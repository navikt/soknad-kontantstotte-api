package no.nav.kontantstotte.api.rest;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Component
@Path("tekster")
public class TeksterResource {

    @GET
    public String tekster() {
        return "tekster";
    }
}
