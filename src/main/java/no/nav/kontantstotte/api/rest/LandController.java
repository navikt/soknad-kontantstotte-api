package no.nav.kontantstotte.api.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.kontantstotte.tekst.TekstService;
import no.nav.security.oidc.api.Unprotected;

@RestController
@RequestMapping("api/land")
@Unprotected
public class LandController {

    private static final String[] VALID_LANGUAGES = {"nb", "nn"};

    private final TekstService tekstService;

    private Map<String, Map<String, String>> landAlleSprak;

    public LandController() {
        this.tekstService = new TekstService();
        this.landAlleSprak = new HashMap<>();
        for (String sprak : VALID_LANGUAGES) {
            landAlleSprak.put(sprak, tekstService.hentLand(sprak));
        }
    }

    @GetMapping(produces = APPLICATION_JSON)
    public Map<String, Map<String, String>> land() {
        return landAlleSprak;
    }
}
