package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.tekst.TekstService;
import no.nav.security.oidc.api.Unprotected;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("tekster")
@Unprotected
public class TeksterResource {

    private static final String[] VALID_LANGUAGES = { "nb", "nn" };

    private final TekstService tekstService;

    private Map<String, Map<String,String>> teksterAlleSprak;

    public TeksterResource() {
        this.tekstService = new TekstService();
        this.teksterAlleSprak = new HashMap<>();
        for (String sprak : VALID_LANGUAGES) {
            teksterAlleSprak.put(sprak, tekstService.hentTekster(sprak));
        }
    }

    @GET
    public Map<String, Map<String, String>> tekster() {
        return teksterAlleSprak;
    }

    @GET
    @Path("{language}")
    public Map<String, String> land(@PathParam("language") String sprak) {
        return tekstService.hentTekster(sprak);
    }
}
