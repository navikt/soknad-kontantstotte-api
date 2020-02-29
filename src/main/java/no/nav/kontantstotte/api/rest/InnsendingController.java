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

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

@RestController
@RequestMapping("api/sendinn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class InnsendingController {

    private final MottakInnsendingService mottakInnsendingService;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(InnsendingController.class);
    private final Counter soknadSendtInn = Metrics.counter("soknad.kontantstotte", "innsending", "mottatt");
    private final Counter soknadSendtInnUgyldig = Metrics.counter("soknad.kontantstotte", "innsending", "ugyldig");

    @Autowired
    public InnsendingController(MottakInnsendingService mottakInnsendingService, ObjectMapper objectMapper) {
        this.mottakInnsendingService = mottakInnsendingService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InnsendingsResponsDto> sendInn(@RequestBody String samletMottakDto) {
        Soknad soknad;
        Søknad kontraktSøknad;

        JsonFactory factory = objectMapper.getFactory();
        JsonParser parser;
        try {
            parser = factory.createParser(samletMottakDto);
            JsonNode actualObj = objectMapper.readTree(parser);
            soknad = objectMapper.treeToValue(actualObj.get("soknad"), Soknad.class);
            kontraktSøknad = SøknadKt.toSøknad(actualObj.get("kontraktSøknad").toString());
        } catch (IOException e) {
            logger.error("Klarte ikke å deserializere søknader. Noen har potensielt forsøkt å sende inn en ugyldig søknad.");
            return ResponseEntity.badRequest().build();
        }

        if (soknad == null || !soknad.erGyldig()) { //TODO denne logikken bør inn i kontraktSøknad
            logger.info("Noen har forsøkt å sende inn en ugyldig søknad.");
            soknadSendtInnUgyldig.increment();
            return ResponseEntity.badRequest().build();
        }

        soknad.markerInnsendingsTidspunkt();
        final var fnr = hentFnrFraToken();
        soknad.setPerson(new Person(fnr, null, null));

        mottakInnsendingService.sendInnSøknad(kontraktSøknad);
        soknadSendtInn.increment();

        return ResponseEntity.ok(new InnsendingsResponsDto(soknad.innsendingsTidspunkt.toString()));
    }
}
