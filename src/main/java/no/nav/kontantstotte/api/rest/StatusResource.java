package no.nav.kontantstotte.api.rest;

import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("status")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class StatusResource {

    @GET
    @Path("ping")
    public String ping() {
        return "pong";
    }
}
