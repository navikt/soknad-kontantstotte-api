package no.nav.kontantstotte.api.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.api.rest.dto.InnsendingsResponsDto;
import no.nav.kontantstotte.innsending.InnsendingService;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("api/sendinn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class InnsendingResource {

    private final InnsendingService innsendingService;
    private final Logger logger = LoggerFactory.getLogger(InnsendingResource.class);
    private final Counter soknadSendtInn = Metrics.counter("soknad.kontantstotte", "innsending", "mottatt");
    private final Counter soknadSendtInnUgyldig = Metrics.counter("soknad.kontantstotte", "innsending", "ugyldig");

    public InnsendingResource(InnsendingService innsendingService) {
        this.innsendingService = innsendingService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<InnsendingsResponsDto> sendInnSoknad(@RequestBody Soknad soknad) {
        if (!soknad.erGyldig()) {
            logger.info("Noen har forsøkt å sende inn en ugyldig søknad.");
            soknadSendtInnUgyldig.increment();
            return ResponseEntity.badRequest().build();
        }
        soknad.markerInnsendingsTidspunkt();
        soknadSendtInn.increment();
        innsendingService.sendInnSoknad(soknad);

        return ResponseEntity.ok(new InnsendingsResponsDto(soknad.innsendingsTidspunkt.toString()));
    }
}
