package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.tekst.DefaultTekstProvider;
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

    private final TekstProvider tekstProvider;

    public TeksterResource() {
        this.tekstProvider = new DefaultTekstProvider();
    }

    @GET
    @Path("{language}")
    public Map<String, String> tekster(@PathParam("language") String sprak) {
        return tekstProvider.hentTekster(sprak);
    }

}
