package no.nav.kontantstotte.api.rest;

import no.nav.security.oidc.api.Unprotected;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Component
@Path("internal")
@Unprotected
public class InternalResource {

    @GET
    @Path("isAlive")
    public String isAlive() {
        return "Ok";
    }
}