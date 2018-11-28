package no.nav.kontantstotte.innsyn.service.rest;

import no.nav.kontantstotte.innsyn.domain.*;
import no.nav.tps.innsyn.PersoninfoDto;
import no.nav.tps.innsyn.RelasjonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static no.nav.kontantstotte.innsyn.service.rest.InnsynConverter.relasjonDtoToBarn;
import static no.nav.kontantstotte.innsyn.service.rest.InnsynConverter.personinfoDtoToPerson;

class InnsynService implements IInnsynService {

    private static final String CONSUMER_ID = "soknad-kontantstotte-api";
    private final Logger logger = LoggerFactory.getLogger(InnsynService.class);

    private final IInnsynClient client;

    @Inject
    InnsynService(Client client, URI tpsInnsynServiceUri) {
        this.client = new InnsynClient(client, tpsInnsynServiceUri);
    }

    @Override
    public Person hentPersonInfo(String fnr) {
        Response response = client.getInnsynResponse("person", fnr);

        PersoninfoDto dto = response.readEntity(PersoninfoDto.class);
        return personinfoDtoToPerson.apply(dto);
    }

    @Override
    public List<Barn> hentBarnInfo(String fnr) {
        Response response = client.getInnsynResponse("barn", fnr);

        List<RelasjonDto> dtoList = response.readEntity(new GenericType<List<RelasjonDto>>() {});
        return dtoList
                .stream()
                .map(dto -> relasjonDtoToBarn.apply(dto))
                .collect(Collectors.toList());
    }
}
