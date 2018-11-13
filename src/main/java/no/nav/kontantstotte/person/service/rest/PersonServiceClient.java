package no.nav.kontantstotte.person.service.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.person.domain.Person;
import no.nav.kontantstotte.person.domain.PersonOppslagException;
import no.nav.kontantstotte.person.domain.PersonService;
import no.nav.log.MDCConstants;
import no.nav.tps.person.PersoninfoDto;
import org.slf4j.MDC;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static no.nav.kontantstotte.person.service.rest.PersonConverter.personinfoDtoToPerson;

class PersonServiceClient implements PersonService {

    private static final String CONSUMER_ID = "soknad-kontantstotte-api";

    private final Counter tpsOppslagSuccess = Metrics.counter("soknad.kontantstotte.tpsoppslag", "response", "success");
    private final Counter tpsOppslagFailure = Metrics.counter("soknad.kontantstotte.tpsoppslag", "response", "failure");

    private final Client client;

    private URI personServiceUri;

    PersonServiceClient(Client client, URI personServiceUri) {

        this.client = client;
        this.personServiceUri = personServiceUri;
    }

    @Override
    public Person hentPersonInfo(String fnr) {
        Response response = client.target(personServiceUri)
                .path("person")
                .request()
                .header("Nav-Call-Id", MDC.get(MDCConstants.MDC_CORRELATION_ID))
                .header("Nav-Consumer-Id", CONSUMER_ID)
                .header("Nav-Personident", fnr)
                .get();

        if(!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            tpsOppslagFailure.increment();
            throw new PersonOppslagException(response.readEntity(String.class));
        }

        PersoninfoDto dto = response.readEntity(PersoninfoDto.class);
        tpsOppslagSuccess.increment();
        return personinfoDtoToPerson.apply(dto);
    }

    @Override
    public void ping() {
        Response response = client.target(personServiceUri)
                .path("/internal/alive")
                .request()
                .get();

        if(!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            throw new PersonOppslagException("TPS person service is not up");
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
