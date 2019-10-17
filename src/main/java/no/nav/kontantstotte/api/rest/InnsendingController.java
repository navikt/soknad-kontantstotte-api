package no.nav.kontantstotte.api.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.familie.ks.kontrakter.søknad.SøknadKt;
import no.nav.kontantstotte.api.rest.dto.InnsendingsResponsDto;
import no.nav.kontantstotte.innsending.ArkivInnsendingService;
import no.nav.kontantstotte.innsending.MottakInnsendingService;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.steg.Person;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static no.nav.kontantstotte.config.toggle.UnleashProvider.toggle;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

@RestController
@RequestMapping("api/sendinn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class InnsendingController {

    private final ArkivInnsendingService arkivInnsendingService;
    private final MottakInnsendingService mottakInnsendingService;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(InnsendingController.class);
    private final Counter soknadSendtInn = Metrics.counter("soknad.kontantstotte", "innsending", "mottatt");
    private final Counter soknadSendtInnUgyldig = Metrics.counter("soknad.kontantstotte", "innsending", "ugyldig");
    public static final String JOURNALFØR_SELV = "kontantstotte.journalfor_selv";

    private static final Logger log = LoggerFactory.getLogger(InnsendingController.class);


    @Autowired
    public InnsendingController(ArkivInnsendingService arkivInnsendingService, MottakInnsendingService mottakInnsendingService, ObjectMapper objectMapper) {
        this.arkivInnsendingService = arkivInnsendingService;
        this.mottakInnsendingService = mottakInnsendingService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InnsendingsResponsDto> sendInn(@RequestBody String samletMottakDto) {
        Soknad soknad;
        Søknad søknad;

        JsonFactory factory = objectMapper.getFactory();
        JsonParser parser;
        try {
            parser = factory.createParser(samletMottakDto);
            JsonNode actualObj = objectMapper.readTree(parser);
            soknad = objectMapper.treeToValue(actualObj.get("soknad"), Soknad.class);
            søknad = SøknadKt.toSøknad(actualObj.get("kontraktSøknad").toString());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Klarte ikke å deserializere søknader. Noen har potensielt forsøkt å sende inn en ugyldig søknad.");
            return ResponseEntity.badRequest().build();
        }

        if (soknad == null || !soknad.erGyldig()) {
            logger.info("Noen har forsøkt å sende inn en ugyldig søknad.");
            soknadSendtInnUgyldig.increment();
            return ResponseEntity.badRequest().build();
        }
        soknad.markerInnsendingsTidspunkt();
        final var fnr = hentFnrFraToken();
        soknad.setPerson(new Person(fnr, null, null));

        log.info("Journalfør selv er satt til: {}", toggle(JOURNALFØR_SELV).isEnabled());
        if (toggle(JOURNALFØR_SELV).isDisabled()) {
            arkivInnsendingService.sendInnSoknad(soknad);
        }

        mottakInnsendingService.sendInnSøknadPåNyttFormat(søknad, toggle(JOURNALFØR_SELV).isEnabled());
        soknadSendtInn.increment();

        return ResponseEntity.ok(new InnsendingsResponsDto(soknad.innsendingsTidspunkt.toString()));
    }
}
