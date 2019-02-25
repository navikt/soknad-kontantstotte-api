package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.tekst.TekstService;
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
@Path("land")
@Unprotected
public class LandResource {

    private final TekstService tekstService;

    private final Logger logger = LoggerFactory.getLogger(LandResource.class);

    public LandResource() {
        this.tekstService = new TekstService();
    }

    @GET
    @Path("{language}")
    public Map<String, String> tekster(@PathParam("language") String sprak) {
        Map<String, String> landMap = tekstService.hentLand(sprak);
        if (landMap == null) {
            logger.info("Forsøkt å hente land på språk som ikke er støttet. Forsøkt språk: " + sprak);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return landMap;
    }

}
