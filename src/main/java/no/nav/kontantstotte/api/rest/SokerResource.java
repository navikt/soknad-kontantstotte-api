package no.nav.kontantstotte.api.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.api.rest.dto.SokerDto;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;


@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("soker")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class SokerResource {
    private final Logger logger = LoggerFactory.getLogger("secureLogger");

    private final InnsynService innsynServiceClient;

    private final Counter soknadApnet = Metrics.counter("soknad.kontantstotte.apnet");
    private final Counter sokerErNorsk = Metrics.counter("soker.land", "land", "NOR");
    private final Counter sokerErIkkeNorsk = Metrics.counter("soker.land", "land", "annet");

    @Inject
    public SokerResource(InnsynService innsynServiceClient) {
        this.innsynServiceClient = innsynServiceClient;
    }

    @GET
    public SokerDto hentPersonInfoOmSoker() {
        String fnr = hentFnrFraToken();

        Person person = innsynServiceClient.hentPersonInfo(fnr);
        logger.info("Henter personinfo på person med fnr: test");

        soknadApnet.increment();
        if ("NOR".equals(person.getStatsborgerskap())) {
            sokerErNorsk.increment();
        } else {
            sokerErIkkeNorsk.increment();
        }

        return new SokerDto(fnr, person.getFornavn(), person.getFulltnavn(), person.getStatsborgerskap());
    }
}
