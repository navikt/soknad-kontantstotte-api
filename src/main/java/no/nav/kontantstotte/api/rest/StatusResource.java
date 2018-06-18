package no.nav.kontantstotte.api.rest;

import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;
import no.nav.security.spring.oidc.validation.api.Unprotected;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

@Component
@Path("status")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class StatusResource {

    @GET
    @Path("ping")
    public String ping() {
        return "pong";
    }

    @GET
    @Path("isAlive")
    @Unprotected
    public String isAlive() {
        return "Ok";
    }
}
