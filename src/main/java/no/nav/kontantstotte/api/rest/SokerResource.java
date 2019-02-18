package no.nav.kontantstotte.api.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.api.rest.dto.SokerDto;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.BRUK_TPS_INTEGRASJON;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;


@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("soker")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class SokerResource {

    private final InnsynService innsynServiceClient;

    private final Logger logger = LoggerFactory.getLogger(SokerResource.class);

    private final Counter soknadApnet = Metrics.counter("soknad.kontantstotte.apnet");

    private static final ObjectMapper mapper = new ObjectMapper();

    @Inject
    public SokerResource(InnsynService innsynServiceClient) {
        this.innsynServiceClient = innsynServiceClient;
    }

    @GET
    public SokerDto hentPersonInfoOmSoker() {
        String fnr = hentFnrFraToken();
        if(UnleashProvider.get().isEnabled(BRUK_TPS_INTEGRASJON)) {
            Person person = innsynServiceClient.hentPersonInfo(fnr);
            soknadApnet.increment();
            return new SokerDto(fnr, person.getFornavn(), person.getFulltnavn(), kodeTilLand(person.getStatsborgerskap()));
        } else {
            return new SokerDto(fnr, null, null, null);
        }
    }

    private String kodeTilLand(String landkode) {
        String land = null;
        try {
            Map<String, String> landMap = mapper.readValue(new File(getFile(
                    "tps/land-mapping.json")), new TypeReference<Map<String, String>>() {
            });
            land = landMap.get(landkode);
        } catch (IOException e) {
            logger.info("Kunne ikke mappe land. Landkode " + landkode + " returnert.");
        }
        return land != null ? land : landkode;
    }

    private String getFile(String filnavn) {
        return getClass().getClassLoader().getResource(filnavn).getFile();
    }
}
