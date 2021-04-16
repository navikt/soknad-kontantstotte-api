package no.nav.kontantstotte.api.rest;

import com.nimbusds.jwt.SignedJWT;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.dokument.FamilieDokumentClient;
import no.nav.kontantstotte.storage.attachment.AttachmentStorage;
import no.nav.kontantstotte.storage.attachment.Format;
import no.nav.kontantstotte.storage.attachment.ImageConversionService;
import no.nav.kontantstotte.storage.encryption.Encryptor;
import no.nav.kontantstotte.storage.s3.TestStorageConfiguration;
import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.test.support.JwtTokenGenerator;
import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;
import org.apache.tika.Tika;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
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
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = {ApplicationConfig.class, TokenGeneratorConfiguration.class, TestStorageConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class StorageControllerTestV2 {
    public static final String INNLOGGET_BRUKER = "12345678911";
    private static final String VEDLEGGS_ID = "UUID321";

    @Value("${local.server.port}")
    private int port;

    private String contextPath = "/api";

    @MockBean
    private FamilieDokumentClient familieDokumentClient;

    @MockBean
    private ImageConversionService imageConverionService;

    @MockBean
    AttachmentStorage s3Storage;

    @Autowired
    private Encryptor encryptor;

    private static final String TEST_PDF = "pdf_dummy.pdf";

    private static final String TEST_PNG = "png_dummy.png";

    @Test
    public void at_vedlegg_puttes_korrekt() throws IOException {
        when(familieDokumentClient.lagreVedlegg(any(), any())).thenReturn(VEDLEGGS_ID);
        byte[] pdfData = readFile(TEST_PDF);
        when(imageConverionService.convert(any(), any())).thenReturn(pdfData);
        byte[] pngData = readFile(TEST_PNG);

        //pdf
        HttpResponse response = postKall(TEST_PDF);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //png
        response = postKall(TEST_PNG);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ArgumentCaptor<byte[]> byteArrayCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        verify(familieDokumentClient, times(2)).lagreVedlegg(byteArrayCaptor.capture(), stringCaptor.capture());

        //The data from pdf file received by FamilieDokumentClient should be encrypted
        assertThat(Arrays.equals(pdfData, byteArrayCaptor.getAllValues().get(0))).isFalse();
        byte[] decrypted = encryptor.decrypt(INNLOGGET_BRUKER, byteArrayCaptor.getAllValues().get(0));
        assertThat(Arrays.equals(pdfData, decrypted)).isTrue();
        assertThat(stringCaptor.getAllValues().get(0)).isEqualTo(TEST_PDF);

        //The data from png file received by FamilieDokumentClient should be encrypted and converted
        assertThat(Arrays.equals(pngData, byteArrayCaptor.getAllValues().get(1))).isFalse();
        decrypted = encryptor.decrypt(INNLOGGET_BRUKER, byteArrayCaptor.getAllValues().get(1));
        assertThat(Arrays.equals(pngData, decrypted)).isFalse();
        Format detectedType = Format.fromMimeType(new Tika().detect(decrypted)).orElse(null);
        assertThat(detectedType).isEqualTo(Format.PDF);

        assertThat(stringCaptor.getAllValues().get(1)).isEqualTo(TEST_PNG);
    }

    @Test
    public void at_vedlegg_hentes_korrekt() throws IOException {
        byte[] encryptedPdf = encryptor.encryptedStream(INNLOGGET_BRUKER,
                                                        new ByteArrayInputStream(readFile(TEST_PDF)))
                .readAllBytes();

        byte[] pdfData = readFile(TEST_PDF);
        when(familieDokumentClient.hentVedlegg(any())).thenReturn(encryptedPdf);
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

    @Test
    public void at_bruke_s3_hvis_vedlegg_ikke_funnes_i_familie_dokument(){
        when(familieDokumentClient.hentVedlegg(any())).thenReturn(null);
        when(s3Storage.get(any(), any())).thenReturn(Optional.of("s3 brukes".getBytes()));
        HttpResponse response = getKall();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isEqualTo("s3 brukes".getBytes());
    }

    private HttpResponse<String> postKall(String filnavn) {
        HttpClient client = HttpClient.newHttpClient();
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);
        String boundary = new BigInteger(256, new Random()).toString();

        Map<Object, Object> multipart = Map.of("file", new File("src/test/resources/dummy/"+ filnavn).toPath());

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


    private HttpResponse<byte[]> getKall() {
        HttpClient client = HttpClient.newHttpClient();
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + contextPath + "/vedlegg/" + VEDLEGGS_ID))
                                             .header(OIDCConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                                             .header("Referer", "https://soknad-kontantstotte-q.nav.no/")
                                             .header("Origin", "https://soknad-kontantstotte-q.nav.no")
                                             .GET()
                                             .build();
            return client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] readFile(String filename) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream fileInput = classloader.getResourceAsStream("dummy/"+ filename);
        return fileInput.readAllBytes();
    }
}
