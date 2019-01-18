package no.nav.kontantstotte.api.rest;

import com.nimbusds.jwt.SignedJWT;
import no.finn.unleash.FakeUnleash;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsending.InnsendingException;
import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.StorageException;
import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.test.support.JwtTokenGenerator;
import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;
import org.assertj.core.api.AssertionsForClassTypes;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.OK;
import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_VEDLEGG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ApplicationConfig.class, TokenGeneratorConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class StorageResourceTest {

    public static final String INNLOGGET_BRUKER = "12345678911";
    private static final String VEDLEGGS_ID = "UUID123";
    private static final String SOKNADS_ID = "SOKNAD123";
    private static final String TESTDATA = "TESTDATA123";

    @Value("${local.server.port}")
    private int port;

    @Value("${spring.jersey.application-path}")
    private String contextPath;

    @Inject
    private Storage attachmentStorage;

    @Before
    public void setUp() {
        FakeUnleash fakeUnleash = new FakeUnleash();
        fakeUnleash.enable(KONTANTSTOTTE_VEDLEGG);
        UnleashProvider.initialize(fakeUnleash);
    }

    @After
    public void tearDown() {
        reset(attachmentStorage);
    }

    @Test
    public void at_vedlegg_puttes_korrekt() throws UnsupportedEncodingException {
        Response response = postKall();
        AssertionsForClassTypes.assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

        ArgumentCaptor<ByteArrayInputStream> streamCaptor = ArgumentCaptor.forClass(ByteArrayInputStream.class);
        verify(attachmentStorage).put(eq(SOKNADS_ID+INNLOGGET_BRUKER), any(String.class), streamCaptor.capture());
        String capturedStream = readStream(streamCaptor.getValue()).toString("UTF-8");
        assertThat(capturedStream).isEqualTo(TESTDATA);
    }

    @Test
    public void at_vedlegg_hentes_korrekt() {
        byte[] streamedTestData = readStream(new ByteArrayInputStream(TESTDATA.getBytes())).toByteArray();
        when(attachmentStorage.get(any(), any())).thenReturn(Optional.ofNullable(streamedTestData));

        Response response = getKall();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        verify(attachmentStorage).get(eq(SOKNADS_ID+INNLOGGET_BRUKER), eq(VEDLEGGS_ID));
        assertThat(response.readEntity(String.class)).isEqualTo(TESTDATA);
    }

    @Test
    public void at_pdfgen_feil_gir_500() {
        doThrow(new InnsendingException("Feil i innsending til pdfgen")).when(attachmentStorage).put(any(), any(), any());
        Response response = postKall();
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    public void at_lagringsfeil_gir_500() {
        doThrow(new StorageException("Feil ved lagring")).when(attachmentStorage).put(any(), any(), any());
        Response response = postKall();
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    private Response postKall() {
        WebTarget target = ClientBuilder.newClient().register(LoggingFeature.class).target("http://localhost:" + port + contextPath);
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);
        return target
                .path("/vedlegg/"+SOKNADS_ID+"/filnavn.pdf")
                .request()
                .header(OIDCConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                .header("Referer", "https://soknad-kontantstotte-t.nav.no/")
                .header("Origin", "https://soknad-kontantstotte-t.nav.no")
                .post(Entity.entity(TESTDATA.getBytes(), MediaType.APPLICATION_OCTET_STREAM));
    }


    private Response getKall() {
        WebTarget target = ClientBuilder.newClient().register(LoggingFeature.class).target("http://localhost:" + port + contextPath);
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);
        return target
                .path("/vedlegg/"+SOKNADS_ID+"/"+VEDLEGGS_ID)
                .request(MediaType.APPLICATION_OCTET_STREAM)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .header(OIDCConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                .header("Referer", "https://soknad-kontantstotte-t.nav.no/")
                .header("Origin", "https://soknad-kontantstotte-t.nav.no")
                .get();
    }

    private ByteArrayOutputStream readStream(ByteArrayInputStream inputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[65536];
        while ((read = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, read);
        }
        return buffer;
    }
}
