package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.tekst.TekstProvider;
import no.nav.security.oidc.api.Unprotected;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("tekster")
@Unprotected
public class TeksterResource {

    private static final String DEFAULT_BUNDLE_NAME = "tekster";

    private static final String[] DEFAULT_VALID_LANGUAGES = { "nb", "nn" };

    private final TekstProvider tekstProvider;

    public TeksterResource() {
        this.tekstProvider = new TekstProvider(DEFAULT_BUNDLE_NAME, DEFAULT_VALID_LANGUAGES);
    }

    @GET
    @Path("{language}")
    public Map<String, String> tekster(@PathParam("language") String sprak) {
        return tekstProvider.hentTekster(sprak);
    }

}
