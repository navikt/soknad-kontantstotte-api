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
@RequestMapping("api/tekster")
@Unprotected
public class TeksterResource {

    private static final String[] VALID_LANGUAGES = {"nb", "nn"};

    private final TekstService tekstService;

    private Map<String, Map<String, String>> teksterAlleSprak;

    public TeksterResource() {
        this.tekstService = new TekstService();
        this.teksterAlleSprak = new HashMap<>();
        for (String sprak : VALID_LANGUAGES) {
            teksterAlleSprak.put(sprak, tekstService.hentTekster(sprak));
        }
    }

    @GetMapping(produces = APPLICATION_JSON)
    public Map<String, Map<String, String>> tekster() {
        return teksterAlleSprak;
    }
}
