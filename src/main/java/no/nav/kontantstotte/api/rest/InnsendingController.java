package no.nav.kontantstotte.api.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.api.rest.dto.InnsendingsResponsDto;
import no.nav.kontantstotte.innsending.ArkivInnsendingService;
import no.nav.kontantstotte.innsending.MottakInnsendingService;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sendinn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class InnsendingController {

    private final ArkivInnsendingService arkivInnsendingService;
    private final MottakInnsendingService mottakInnsendingService;
    private final Logger logger = LoggerFactory.getLogger(InnsendingController.class);
    private final Counter soknadSendtInn = Metrics.counter("soknad.kontantstotte", "innsending", "mottatt");
    private final Counter soknadSendtInnUgyldig = Metrics.counter("soknad.kontantstotte", "innsending", "ugyldig");

    public InnsendingController(ArkivInnsendingService arkivInnsendingService, MottakInnsendingService mottakInnsendingService) {
        this.arkivInnsendingService = arkivInnsendingService;
        this.mottakInnsendingService = mottakInnsendingService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InnsendingsResponsDto> sendInnSoknad(@RequestBody Soknad soknad) {
        if (!soknad.erGyldig()) {
            logger.info("Noen har forsøkt å sende inn en ugyldig søknad.");
            soknadSendtInnUgyldig.increment();
            return ResponseEntity.badRequest().build();
        }
        soknad.markerInnsendingsTidspunkt();
        soknadSendtInn.increment();
        arkivInnsendingService.sendInnSoknad(soknad);
        mottakInnsendingService.sendInnSoknad(soknad);

        return ResponseEntity.ok(new InnsendingsResponsDto(soknad.innsendingsTidspunkt.toString()));
    }
}
