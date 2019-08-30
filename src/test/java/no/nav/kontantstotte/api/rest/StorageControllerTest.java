package no.nav.kontantstotte.api.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.storage.attachment.AttachmentStorage;
import no.nav.kontantstotte.storage.s3.TestStorageConfiguration;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.nimbusds.jwt.SignedJWT;

import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.innsending.InnsendingException;
import no.nav.kontantstotte.storage.StorageException;
import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.test.support.JwtTokenGenerator;
import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;


@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ApplicationConfig.class, TokenGeneratorConfiguration.class, TestStorageConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class StorageControllerTest {

    public static final String INNLOGGET_BRUKER = "12345678911";
    private static final String VEDLEGGS_ID = "UUID123";
    private static final String TESTDATA = "TESTDATA123";

    @Value("${local.server.port}")
    private int port;

    private String contextPath = "/api";

    @Autowired
    private AttachmentStorage attachmentStorage;

    @After
    public void tearDown() {
        reset(attachmentStorage);
    }

    @Test
    public void at_vedlegg_puttes_korrekt() throws IOException {
        HttpResponse response = postKall();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ArgumentCaptor<InputStream> streamCaptor = ArgumentCaptor.forClass(InputStream.class);
        verify(attachmentStorage).put(eq(INNLOGGET_BRUKER), any(String.class), streamCaptor.capture());
        File capturedFile = readStream(streamCaptor.getValue());
        assertThat(Files.readAllBytes(capturedFile.toPath())).isEqualTo(Files.readAllBytes(new File("src/test/resources/dummy/png_dummy.png").toPath()));
    }

    @Test
    public void at_vedlegg_hentes_korrekt() throws IOException {
        byte[] streamedTestData = Files.readAllBytes(readStream(new ByteArrayInputStream(TESTDATA.getBytes())).toPath());
        when(attachmentStorage.get(any(), any())).thenReturn(Optional.ofNullable(streamedTestData));

        HttpResponse<String> response = getKall();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        verify(attachmentStorage).get(eq(INNLOGGET_BRUKER), eq(VEDLEGGS_ID));
        assertThat(response.body()).isEqualTo(TESTDATA);
    }

    @Test
    public void at_pdfgen_feil_gir_500() {
        doThrow(new InnsendingException("Feil i innsending til pdfgen")).when(attachmentStorage).put(any(), any(), any());
        HttpResponse response = postKall();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    public void at_lagringsfeil_gir_500() {
        doThrow(new StorageException("Feil ved lagring")).when(attachmentStorage).put(any(), any(), any());
        HttpResponse response = postKall();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private HttpResponse<String> postKall() {
        HttpClient client = HttpClient.newHttpClient();
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);
        String boundary = new BigInteger(256, new Random()).toString();

        Map<Object, Object> multipart = Map.of("file", new File("src/test/resources/dummy/png_dummy.png").toPath());

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + contextPath + "/vedlegg/"))
                    .header(OIDCConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                    .header(HttpHeader.CONTENT_TYPE.asString(), "multipart/form-data;boundary=" + boundary)
                    .POST(MultipartBodyPublisher.ofMimeMultipartData(multipart, boundary))
                    .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }


    private HttpResponse<String> getKall() {
        HttpClient client = HttpClient.newHttpClient();
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + contextPath + "/vedlegg/" + VEDLEGGS_ID))
                    .header(OIDCConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                    .header("Referer", "https://soknad-kontantstotte-q.nav.no/")
                    .header("Origin", "https://soknad-kontantstotte-q.nav.no")
                    .GET()
                    .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private File readStream(InputStream inputStream) throws IOException {
        File targetFile = File.createTempFile("targetFile", "tmp");
        targetFile.deleteOnExit();
        OutputStream outStream = new FileOutputStream(targetFile);

        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outStream);
        return targetFile;
    }
}
