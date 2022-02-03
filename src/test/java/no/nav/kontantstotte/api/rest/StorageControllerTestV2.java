package no.nav.kontantstotte.api.rest;

import com.nimbusds.jwt.SignedJWT;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.dokument.FamilieDokumentClient;
import no.nav.security.token.support.core.JwtTokenConstants;
import no.nav.security.token.support.test.JwtTokenGenerator;
import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = {ApplicationConfig.class, TokenGeneratorConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class StorageControllerTestV2 {

    public static final String INNLOGGET_BRUKER = "12345678911";
    private static final String VEDLEGGS_ID = "UUID321";

    @Value("${local.server.port}")
    private int port;

    private String contextPath = "/api";

    @MockBean
    private FamilieDokumentClient familieDokumentClient;

    private static final String TEST_PDF = "pdf_dummy.pdf";

    @Test
    public void at_vedlegg_puttes_korrekt() throws IOException {
        when(familieDokumentClient.lagreVedlegg(any(), any())).thenReturn(VEDLEGGS_ID);
        byte[] pdfData = readFile();

        HttpResponse response = postKall(TEST_PDF);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ArgumentCaptor<byte[]> byteArrayCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        verify(familieDokumentClient).lagreVedlegg(byteArrayCaptor.capture(), stringCaptor.capture());

        assertThat(Arrays.equals(pdfData, byteArrayCaptor.getValue())).isTrue();
        assertThat(stringCaptor.getAllValues().get(0)).isEqualTo(TEST_PDF);
    }

    @Test
    public void at_vedlegg_hentes_korrekt() throws IOException {
        byte[] pdfData = readFile();
        when(familieDokumentClient.hentVedlegg(any())).thenReturn(pdfData);
        HttpResponse<byte[]> response = getKall();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        verify(familieDokumentClient).hentVedlegg(eq(VEDLEGGS_ID));
        assertThat(response.body()).isEqualTo(pdfData);
    }

    @Test
    public void at_lagringsfeil_gir_500() {
        when(familieDokumentClient.lagreVedlegg(any(), any())).thenReturn(null);
        HttpResponse response = postKall(TEST_PDF);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private HttpResponse<String> postKall(String filnavn) {
        HttpClient client = HttpClient.newHttpClient();
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);
        String boundary = new BigInteger(256, new Random()).toString();

        Map<Object, Object> multipart = Map.of("file", new File("src/test/resources/dummy/" + filnavn).toPath());

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + contextPath + "/vedlegg/"))
                                             .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                                             .header(HttpHeader.CONTENT_TYPE.asString(),
                                                     "multipart/form-data;boundary=" + boundary)
                                             .POST(MultipartBodyPublisher.ofMimeMultipartData(multipart, boundary))
                                             .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }


    private HttpResponse<byte[]> getKall() {
        HttpClient client = HttpClient.newHttpClient();
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);
        try {
            HttpRequest request =
                    HttpRequest.newBuilder(URI.create("http://localhost:" + port + contextPath + "/vedlegg/" + VEDLEGGS_ID))
                               .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                               .GET()
                               .build();
            return client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] readFile() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream fileInput = classloader.getResourceAsStream("dummy/" + StorageControllerTestV2.TEST_PDF);
        return Objects.requireNonNull(fileInput).readAllBytes();
    }
}
