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
@Path("land")
@Unprotected
public class LandResource {

    private static final String[] VALID_LANGUAGES = { "nb", "nn" };

    private final TekstService tekstService;

    private Map<String, Map<String,String>> landAlleSprak;

    public LandResource() {
        this.tekstService = new TekstService();
        this.landAlleSprak = new HashMap<>();
        for (String sprak : VALID_LANGUAGES) {
            landAlleSprak.put(sprak, tekstService.hentLand(sprak));
        }
    }

    @GET
    public Map<String, Map<String, String>> land() {
        return landAlleSprak;
    }
}
