package no.nav.kontantstotte.api.rest;

import no.nav.security.spring.oidc.validation.api.Unprotected;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("status")
@Unprotected
public class StatusResource {

    @GET
    @Path("ping")
    public String ping() {
        return "pong";
    }
}
