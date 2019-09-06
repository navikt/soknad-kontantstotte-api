package no.nav.kontantstotte.api.rest;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.api.rest.dto.SokerDto;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.security.oidc.api.ProtectedWithClaims;


@RestController
@RequestMapping("api/soker")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class SokerController {
    private final InnsynService innsynServiceClient;

    private final Counter soknadApnet = Metrics.counter("soknad.kontantstotte.apnet");
    private final Counter sokerErNorsk = Metrics.counter("soker.land", "land", "NOR");
    private final Counter sokerErIkkeNorsk = Metrics.counter("soker.land", "land", "annet");

    @Autowired
    public SokerController(InnsynService innsynServiceClient) {
        this.innsynServiceClient = innsynServiceClient;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SokerDto hentPersonInfoOmSoker() {
        String fnr = hentFnrFraToken();

        Person person = innsynServiceClient.hentPersonInfo(fnr);

        soknadApnet.increment();
        if ("NOR".equals(person.getStatsborgerskap())) {
            sokerErNorsk.increment();
        } else {
            sokerErIkkeNorsk.increment();
        }

        return new SokerDto(fnr, person.getFornavn(), person.getFulltnavn(), person.getStatsborgerskap());
    }
}
