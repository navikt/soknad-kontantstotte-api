package no.nav.kontantstotte.api.rest;

import no.nav.security.oidc.api.Unprotected;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("barn")
@Unprotected
public class BarnResource {

    public BarnResource() {}

    @GET
    public List<Object> tekster() {
        return new ArrayList<>();
    }
}
