package no.nav.kontantstotte.person.service.rest;

import no.nav.kontantstotte.person.domain.Person;
import no.nav.kontantstotte.person.domain.PersonService;
import no.nav.kontantstotte.person.domain.PersonServiceException;
import no.nav.log.MDCConstants;
import no.nav.sbl.rest.RestUtils;
import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import no.nav.tps.person.PersoninfoDto;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.MDC;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static no.nav.kontantstotte.person.service.rest.PersonConverter.personinfoDtoToPerson;

class PersonServiceClient implements PersonService {

    private static final String CONSUMER_ID = "soknad-kontantstotte-proxy";

    private final Client client;

    private URI personServiceUri;

    PersonServiceClient(Client client, URI personServiceUri) {

        this.client = client;
        this.personServiceUri = personServiceUri;
    }

    @Override
    public Person hentPersonInfo(String fnr) throws PersonServiceException {
        PersoninfoDto dto = client.target(personServiceUri)
                .path("person")
                .request()
                .header("Nav-Call-Id", MDC.get(MDCConstants.MDC_CORRELATION_ID))
                .header("Nav-Consumer-Id", CONSUMER_ID)
                .header("Nav-Ident", fnr)
                .get(PersoninfoDto.class);

        return personinfoDtoToPerson.apply(dto);
    }

    @Override
    public void ping() {
        Response response = client.target(personServiceUri)
                .path("/internal/alive")
                .request()
                .get();

        if(!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            throw new WebApplicationException("TPS person service is not up", response.getStatus());
        }
    }

    @Override
    public String toString() {
        return "PersonServiceClient{" +
                "client=" + client +
                ", personServiceUri=" + personServiceUri +
                '}';
    }
}
