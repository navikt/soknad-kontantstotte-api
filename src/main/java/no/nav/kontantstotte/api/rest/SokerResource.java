package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.api.rest.dto.SokerDto;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static no.nav.kontantstotte.oppsummering.innsending.ArkivInnsendingService.hentFnrFraToken;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("soker")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class SokerResource {
    @GET
    public SokerDto hentInfoOmSoker() {
        return new SokerDto(hentFnrFraToken());
    }
}
