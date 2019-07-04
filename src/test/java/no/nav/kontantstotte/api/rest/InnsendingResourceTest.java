package no.nav.kontantstotte.api.rest;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Random;

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
import no.nav.kontantstotte.innsending.InnsendingService;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.test.support.JwtTokenGenerator;
import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ApplicationConfig.class, TokenGeneratorConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class InnsendingResourceTest {

    private static final String INNLOGGET_BRUKER = "12345678911";

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @MockBean
    private InnsendingService innsendingService;

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private String contextPath = "/api";

    @Test
    public void at_innsending_av_soknad_er_ok_med_bekreftelse() throws JsonProcessingException {
        Soknad soknadMedBekreftelse = new Soknad();
        soknadMedBekreftelse.oppsummering.bekreftelse = "JA";
        soknadMedBekreftelse.veiledning.bekreftelse = "JA";

        when(innsendingService.sendInnSoknad(any(Soknad.class)))
                .thenReturn(soknadMedBekreftelse);


        HttpResponse<String> response = utførRequest(soknadMedBekreftelse);

        assertThat(response.statusCode()).isEqualTo(OK.getStatusCode());

    }

    @Test
    public void at_soknad_markeres_med_innsendingstidspunkt() throws JsonProcessingException {
        Soknad soknadMedBekreftelse = new Soknad();
        soknadMedBekreftelse.oppsummering.bekreftelse = "JA";
        soknadMedBekreftelse.veiledning.bekreftelse = "JA";


        when(innsendingService.sendInnSoknad(any(Soknad.class)))
                .thenReturn(soknadMedBekreftelse);

        utførRequest(soknadMedBekreftelse);

        ArgumentCaptor<Soknad> captor = ArgumentCaptor.forClass(Soknad.class);
        verify(innsendingService).sendInnSoknad(captor.capture());

        assertThat(captor.getValue().innsendingsTidspunkt).isBefore(now());
        assertThat(captor.getValue().innsendingsTidspunkt).isAfter(now().minus(5, MINUTES));


    }

    @Test
    public void at_innsending_av_soknad_er_gir_400_ved_manglende_bekreftelse() throws JsonProcessingException {

        HttpResponse<String> response = utførRequest(new Soknad());

        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.getStatusCode());

        verifyNoMoreInteractions(innsendingService);
    }

    private HttpResponse<String> utførRequest(Soknad soknad) {
        HttpClient client = HttpClientUtil.create();

        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);
        String boundary = new BigInteger(256, new Random()).toString();

        try {
            Map<Object, Object> multipart = Map.of("soknad", objectMapper.writeValueAsString(soknad) + ";type=application/json");

            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + contextPath + "/sendinn"))
                    .header(HttpHeader.CONTENT_TYPE.asString(), "multipart/form-data;boundary=" + boundary)
                    .header(OIDCConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                    .header("Referer", "https://soknad-kontantstotte-t.nav.no/")
                    .header("Origin", "https://soknad-kontantstotte-t.nav.no")
                    .POST(MultipartBodyPublisher.ofMimeMultipartData(multipart, boundary))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
