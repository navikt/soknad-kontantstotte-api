package no.nav.kontantstotte.innsyn.service.rest;

import no.nav.kontantstotte.innsyn.domain.*;
import no.nav.log.MDCConstants;
import no.nav.tps.innsyn.PersoninfoDto;
import no.nav.tps.innsyn.RelasjonDto;
import org.slf4j.MDC;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static no.nav.kontantstotte.innsyn.service.rest.InnsynConverter.relasjonDtoToBarn;
import static no.nav.kontantstotte.innsyn.service.rest.InnsynConverter.personinfoDtoToPerson;

class InnsynServiceClient implements IInnsynServiceClient {

    private static final String CONSUMER_ID = "soknad-kontantstotte-api";

    private static final Integer MIN_ALDER_I_MANEDER = 10;

    private static final Integer MAKS_ALDER_I_MANEDER = 28;

    private URI tpsInnsynServiceUri;

    private final Client client;

    @Inject
    InnsynServiceClient(Client client, URI tpsInnsynServiceUri) {
        this.client = client;
        this.tpsInnsynServiceUri = tpsInnsynServiceUri;
    }

    @Override
    public Person hentPersonInfo(String fnr) {
        Response response = getInnsynResponse("person", fnr);

        PersoninfoDto dto = response.readEntity(PersoninfoDto.class);
        return personinfoDtoToPerson.apply(dto);
    }

    public static boolean erIKontantstotteAlder(String fodselsdato) {
        Period diff = Period.between(LocalDate.parse(fodselsdato), LocalDate.now());
        Integer alderIManeder = diff.getYears() * 12 + diff.getMonths();
        return (alderIManeder >= MIN_ALDER_I_MANEDER) &&
                (alderIManeder <= MAKS_ALDER_I_MANEDER) &&
                !(alderIManeder == MAKS_ALDER_I_MANEDER && diff.getDays() > 0);
    }

    @Override
    public List<Barn> hentBarnInfo(String fnr) {
        Response response = getInnsynResponse("barn", fnr);

        List<RelasjonDto> dtoList = response.readEntity(new GenericType<List<RelasjonDto>>() {});
        return dtoList
                .stream()
                .filter(dto -> erIKontantstotteAlder(dto.getFoedselsdato()))
                .filter(dto -> dto.isHarSammeAdresse())
                .map(dto -> relasjonDtoToBarn.apply(dto))
                .collect(Collectors.toList());
    }

    @Override
    public void ping() {
        Response response = client.target(tpsInnsynServiceUri)
                .path("/internal/alive")
                .request()
                .get();

        if(!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            throw new InnsynOppslagException("TPS innsyn service is not up");
        }
    }

    @Override
    public String toString() {
        return "InnsynServiceClient{" +
                "client=" + client +
                ", tpsInnsynServiceUri=" + tpsInnsynServiceUri +
                '}';
    }

    public Response getInnsynResponse(String path, String fnr) {
        Response response = client.target(tpsInnsynServiceUri)
                .path(path)
                .request()
                .header("Nav-Call-Id", MDC.get(MDCConstants.MDC_CORRELATION_ID))
                .header("Nav-Consumer-Id", CONSUMER_ID)
                .header("Nav-Personident", fnr)
                .get();

        if(!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            throw new InnsynOppslagException(response.readEntity(String.class));
        } else {
            return response;
        }
    }
}
