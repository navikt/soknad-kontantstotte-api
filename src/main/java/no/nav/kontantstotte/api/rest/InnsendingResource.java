package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static java.time.LocalDateTime.now;

@Component
@Path("sendinn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class InnsendingResource {

    @POST
    public void sendInnSoknad(Soknad soknad) {
        soknad.innsendingTimestamp = now();

    }
}
