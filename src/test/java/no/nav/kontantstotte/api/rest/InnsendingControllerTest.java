package no.nav.kontantstotte.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.familie.ks.kontrakter.søknad.testdata.SøknadTestdata;
import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.innsending.MottakInnsendingService;
import no.nav.kontantstotte.innsending.SamletInnsendingDto;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.security.token.support.core.JwtTokenConstants;
import no.nav.security.token.support.test.JwtTokenGenerator;
import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = {ApplicationConfig.class, TokenGeneratorConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class InnsendingControllerTest {

    private static final String INNLOGGET_BRUKER = "12345678911";

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @MockBean
    private MottakInnsendingService mottakInnsendingService;

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void at_innsending_av_ny_søknad_er_ok() {
        Soknad soknadMedBekreftelse = new Soknad();
        soknadMedBekreftelse.oppsummering.bekreftelse = "JA";
        soknadMedBekreftelse.veiledning.bekreftelse = "JA";

        Søknad testSøknad = SøknadTestdata.enForelderIUtlandUtenBarnehageplass();
        when(mottakInnsendingService.sendInnSøknad(any(Søknad.class))).thenReturn(testSøknad);

        HttpResponse<String> response = utførRequest(new SamletInnsendingDto(soknadMedBekreftelse, testSøknad));

        assertThat(response.statusCode()).isEqualTo(OK.getStatusCode());
    }

    @Test
    public void at_innsending_av_soknad_er_gir_400_ved_manglende_bekreftelse() {

        HttpResponse<String> response =
                utførRequest(new SamletInnsendingDto(new Soknad(), SøknadTestdata.norskFamilieUtenBarnehageplass()));

        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.getStatusCode());

        verifyNoMoreInteractions(mottakInnsendingService);
    }


    @Test
    public void at_vi_journalfører() {
        Soknad soknadMedBekreftelse = new Soknad();
        soknadMedBekreftelse.oppsummering.bekreftelse = "JA";
        soknadMedBekreftelse.veiledning.bekreftelse = "JA";
        Søknad testSøknad = SøknadTestdata.enForelderIUtlandUtenBarnehageplass();


        when(mottakInnsendingService.sendInnSøknad(any(Søknad.class)))
                .thenReturn(testSøknad);

        utførRequest(new SamletInnsendingDto(soknadMedBekreftelse, testSøknad));

        verify(mottakInnsendingService).sendInnSøknad(any(Søknad.class));
    }

    private HttpResponse<String> utførRequest(SamletInnsendingDto samletInnsendingDto) {
        HttpClient client = HttpClientUtil.create();

        String signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER).serialize();

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/sendinn"))
                                             .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.APPLICATION_JSON)
                                             .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT)
                                             .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(
                                                     samletInnsendingDto)))
                                             .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
