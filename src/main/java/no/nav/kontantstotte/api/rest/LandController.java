package no.nav.kontantstotte.api.rest;

import java.util.HashMap;
import java.util.Map;

import no.nav.security.token.support.core.api.Unprotected;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.kontantstotte.tekst.TekstService;

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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Map<String, String>> land() {
        return landAlleSprak;
    }
}
