package no.nav.kontantstotte.api.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.api.rest.dto.InnsendingsResponsDto;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsending.ArkivInnsendingService;
import no.nav.kontantstotte.innsending.MottakInnsendingService;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.steg.Person;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;
import static no.nav.kontantstotte.config.toggle.UnleashProvider.toggle;
//import static no.nav.kontantstotte.config.toggle.UnleashProvider;

@RestController
@RequestMapping("api/sendinn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class InnsendingController {

    private final ArkivInnsendingService arkivInnsendingService;
    private final MottakInnsendingService mottakInnsendingService;
    private final Logger logger = LoggerFactory.getLogger(InnsendingController.class);
    private final Counter soknadSendtInn = Metrics.counter("soknad.kontantstotte", "innsending", "mottatt");
    private final Counter soknadSendtInnUgyldig = Metrics.counter("soknad.kontantstotte", "innsending", "ugyldig");
    public static final String JOURNALFOR_SELV = "kontantstotte.journalfor_selv";

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
        final var fnr = hentFnrFraToken();
        soknad.setPerson(new Person(fnr, null, null));

        boolean journalforSelv = toggle(JOURNALFOR_SELV).isEnabled();
        if (!journalforSelv) {
            arkivInnsendingService.sendInnSoknad(soknad, journalforSelv);
        }
        mottakInnsendingService.sendInnSoknad(soknad, journalforSelv);
        soknadSendtInn.increment();

        return ResponseEntity.ok(new InnsendingsResponsDto(soknad.innsendingsTidspunkt.toString()));
    }
}
