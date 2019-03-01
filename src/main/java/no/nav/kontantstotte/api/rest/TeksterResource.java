package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.tekst.TekstService;
import no.nav.security.oidc.api.Unprotected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("tekster")
@Unprotected
public class TeksterResource {

    private static final String[] VALID_LANGUAGES = { "nb", "nn" };

    private final TekstService tekstService;

    private final Logger logger = LoggerFactory.getLogger(TeksterResource.class);

    public TeksterResource() {
        this.tekstService = new TekstService();
    }

    @GET
    public Map<String, Map<String, String>> tekster() {
        Map<String, Map<String,String>> teksterAlleSprak = new HashMap<>();
        for (String sprak : VALID_LANGUAGES) {
            teksterAlleSprak.put(sprak, teksterPaSprak(sprak));
        }
        return teksterAlleSprak;
    }

    private Map<String, String> teksterPaSprak(String sprak) {
        Map<String, String> tekster = tekstService.hentTekster(sprak);
        if (tekster == null) {
            logger.info("Forsøkt å hente tekster på språk som ikke er støttet. Forsøkt språk: " + sprak);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return tekster;
    }
}
