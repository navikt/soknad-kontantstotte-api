package no.nav.kontantstotte.api.rest;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static no.nav.kontantstotte.api.rest.InnsendingController.JOURNALFOR_SELV;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static no.nav.kontantstotte.config.toggle.UnleashProvider.toggle;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import no.finn.unleash.FakeUnleash;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsending.ArkivInnsendingService;
import no.nav.kontantstotte.innsending.MottakInnsendingService;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;

import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.test.support.JwtTokenGenerator;
import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;

import javax.ws.rs.core.MediaType;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ApplicationConfig.class, TokenGeneratorConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class InnsendingControllerTest {

    private static final String INNLOGGET_BRUKER = "12345678911";

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @MockBean
    private ArkivInnsendingService arkivInnsendingService;

    @MockBean
    private MottakInnsendingService mottakInnsendingService;

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private FakeUnleash unleash = new FakeUnleash();

    @Test
    public void at_innsending_av_soknad_er_ok_med_bekreftelse() {
        Soknad soknadMedBekreftelse = new Soknad();
        soknadMedBekreftelse.oppsummering.bekreftelse = "JA";
        soknadMedBekreftelse.veiledning.bekreftelse = "JA";

        when(arkivInnsendingService.sendInnSoknad(any(Soknad.class), anyBoolean()))
                .thenReturn(soknadMedBekreftelse);
        when(mottakInnsendingService.sendInnSoknad(any(Soknad.class), anyBoolean()))
                .thenReturn(soknadMedBekreftelse);

        HttpResponse<String> response = utførRequest(soknadMedBekreftelse);

        assertThat(response.statusCode()).isEqualTo(OK.getStatusCode());

    }

    @Test
    public void at_soknad_markeres_med_innsendingstidspunkt() throws JsonProcessingException {
        Soknad soknadMedBekreftelse = new Soknad();
        soknadMedBekreftelse.oppsummering.bekreftelse = "JA";
        soknadMedBekreftelse.veiledning.bekreftelse = "JA";

        when(arkivInnsendingService.sendInnSoknad(any(Soknad.class), anyBoolean()))
                .thenReturn(soknadMedBekreftelse);
        when(mottakInnsendingService.sendInnSoknad(any(Soknad.class), anyBoolean()))
                .thenReturn(soknadMedBekreftelse);

        utførRequest(soknadMedBekreftelse);

        ArgumentCaptor<Soknad> captor = ArgumentCaptor.forClass(Soknad.class);
        verify(arkivInnsendingService).sendInnSoknad(captor.capture(),  eq(false));

        assertThat(captor.getValue().innsendingsTidspunkt).isBefore(now());
        assertThat(captor.getValue().innsendingsTidspunkt).isAfter(now().minus(5, MINUTES));


    }

    @Test
    public void at_innsending_av_soknad_er_gir_400_ved_manglende_bekreftelse() throws JsonProcessingException {

        HttpResponse<String> response = utførRequest(new Soknad());

        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.getStatusCode());

        verifyNoMoreInteractions(arkivInnsendingService);
        verifyNoMoreInteractions(mottakInnsendingService);
    }

    @Test
    public void at_vi_fremdeles_journalfører_via_proxy() throws JsonProcessingException {
        UnleashProvider.initialize(unleash);
        unleash.disable(JOURNALFOR_SELV);

        Soknad soknadMedBekreftelse = new Soknad();
        soknadMedBekreftelse.oppsummering.bekreftelse = "JA";
        soknadMedBekreftelse.veiledning.bekreftelse = "JA";

        when(arkivInnsendingService.sendInnSoknad(any(Soknad.class), anyBoolean()))
                .thenReturn(soknadMedBekreftelse);
        when(mottakInnsendingService.sendInnSoknad(any(Soknad.class), anyBoolean()))
                .thenReturn(soknadMedBekreftelse);

        utførRequest(soknadMedBekreftelse);

        verify(arkivInnsendingService).sendInnSoknad(any(Soknad.class), anyBoolean());
        verify(mottakInnsendingService).sendInnSoknad(any(Soknad.class), anyBoolean());
    }

    @Test
    public void at_vi_journalfører_selv() throws JsonProcessingException {
        Soknad soknadMedBekreftelse = new Soknad();
        soknadMedBekreftelse.oppsummering.bekreftelse = "JA";
        soknadMedBekreftelse.veiledning.bekreftelse = "JA";

        when(arkivInnsendingService.sendInnSoknad(any(Soknad.class), anyBoolean()))
                .thenReturn(soknadMedBekreftelse);
        when(mottakInnsendingService.sendInnSoknad(any(Soknad.class), anyBoolean()))
                .thenReturn(soknadMedBekreftelse);

        UnleashProvider.initialize(unleash);
        unleash.enable(JOURNALFOR_SELV);

        utførRequest(soknadMedBekreftelse);

        verifyZeroInteractions(arkivInnsendingService);
        verify(mottakInnsendingService).sendInnSoknad(any(Soknad.class), anyBoolean());
    }

    private HttpResponse<String> utførRequest(Soknad soknad) {
        HttpClient client = HttpClientUtil.create();

        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/sendinn"))
                    .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.APPLICATION_JSON)
                    .header(OIDCConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(soknad)))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
