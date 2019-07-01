package no.nav.kontantstotte.innsyn.service.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import no.nav.kontantstotte.innsyn.domain.Barn;
import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.domain.Person;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static no.nav.kontantstotte.innsyn.service.rest.InnsynConverter.personinfoDtoToPerson;
import static no.nav.kontantstotte.innsyn.service.rest.InnsynConverter.relasjonDtoToBarn;

class InnsynServiceClient implements InnsynService {

    private static final String CONSUMER_ID = "soknad-kontantstotte-api";

    private static final Integer MIN_ALDER_I_MANEDER = 10;

    private static final Integer MAKS_ALDER_I_MANEDER = 28;

    private URI tpsInnsynServiceUri;

    private final Client client;

    private final Counter sokerErIkkeKvalifisert = Metrics.counter("soknad.kontantstotte.kvalifisert", "status", "NEI");
    private final Counter sokerErKvalifisert = Metrics.counter("soknad.kontantstotte.kvalifisert", "status", "JA");
    private final Timer tpsResponstid = Metrics.timer("tps.respons.tid");

    @Inject
    InnsynServiceClient(Client client, URI tpsInnsynServiceUri) {
        this.client = client;
        this.tpsInnsynServiceUri = tpsInnsynServiceUri;
    }

    @Override
    public Person hentPersonInfo(String fnr) {
        long startTime = System.nanoTime();
        Response response = getInnsynResponse("person", fnr);
        tpsResponstid.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);

        PersoninfoDto dto = response.readEntity(PersoninfoDto.class);
        return personinfoDtoToPerson.apply(dto);
    }

    public static boolean erIKontantstotteAlder(String fodselsdato) {
        Period diff = Period.between(LocalDate.parse(fodselsdato), LocalDate.now());
        Integer alderIManeder = diff.getYears() * 12 + diff.getMonths();
        return (alderIManeder >= MIN_ALDER_I_MANEDER) &&
                (alderIManeder <= MAKS_ALDER_I_MANEDER) &&
                !(alderIManeder.equals(MAKS_ALDER_I_MANEDER) && diff.getDays() > 0);
    }

    @Override
    public List<Barn> hentBarnInfo(String fnr) {
        long startTime = System.nanoTime();
        Response response = getInnsynResponse("barn", fnr);
        tpsResponstid.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);

        List<RelasjonDto> dtoList = response.readEntity(new GenericType<List<RelasjonDto>>() {});
        List<Barn> returListe = dtoList
                .stream()
                .filter(dto -> erIKontantstotteAlder(dto.getFoedselsdato()))
                .filter(RelasjonDto::isHarSammeAdresse)
                .map(dto -> relasjonDtoToBarn.apply(dto))
                .collect(Collectors.toList());

        if (returListe.isEmpty()) {
            sokerErIkkeKvalifisert.increment();
        } else {
            sokerErKvalifisert.increment();
        }

        return returListe;
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

    private Response getInnsynResponse(String path, String fnr) {
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
