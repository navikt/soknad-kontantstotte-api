package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.tekst.DefaultTekstProvider;
import no.nav.kontantstotte.tekst.TekstProvider;
import no.nav.security.oidc.api.Unprotected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("tekster")
@Unprotected
public class TeksterResource {

    private final TekstProvider tekstProvider;

    private final Logger logger = LoggerFactory.getLogger(TeksterResource.class);

    public TeksterResource() {
        this.tekstProvider = new DefaultTekstProvider();
    }

    @GET
    @Path("{language}")
    public Map<String, String> tekster(@PathParam("language") String sprak) {
        Map<String, String> tekster = tekstProvider.hentTekster(sprak);
        if (tekster == null) {
            logger.info("Forsøkt å hente språk som ikke er støttet. Forsøkt språk: " + sprak);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return tekster;
    }

}
