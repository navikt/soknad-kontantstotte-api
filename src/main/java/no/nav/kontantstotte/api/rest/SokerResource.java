package no.nav.kontantstotte.api.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.api.rest.dto.SokerDto;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsyn.domain.IInnsynServiceClient;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.BRUK_TPS_INTEGRASJON;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;


@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("soker")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class SokerResource {

    private final IInnsynServiceClient innsynServiceClient;

    private final Counter soknadApnet = Metrics.counter("soknad.kontantstotte.apnet");

    @Inject
    public SokerResource(IInnsynServiceClient innsynServiceClient) {
        this.innsynServiceClient = innsynServiceClient;
    }

    @GET
    public SokerDto hentPersonInfoOmSoker() {
        String fnr = hentFnrFraToken();
        if(UnleashProvider.get().isEnabled(BRUK_TPS_INTEGRASJON)) {
            Person person = innsynServiceClient.hentPersonInfo(fnr);
            soknadApnet.increment();
            return new SokerDto(fnr, person.getFornavn(), person.getFulltnavn());
        } else {
            return new SokerDto(fnr, null, null);
        }
    }

}
