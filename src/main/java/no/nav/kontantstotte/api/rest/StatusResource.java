package no.nav.kontantstotte.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("status")
public class StatusResource {

    @GET
    @Path("ping")
    public String ping() {
        return "pong";
    }
}
