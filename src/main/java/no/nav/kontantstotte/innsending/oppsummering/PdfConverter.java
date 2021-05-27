package no.nav.kontantstotte.innsending.oppsummering;

import com.fasterxml.jackson.core.JsonProcessingException;
import no.nav.familie.kontrakter.felles.Ressurs;
import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.client.TokenHelper;
import no.nav.kontantstotte.innsending.InnsendingException;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
class PdfConverter {

    private static final Logger log = LoggerFactory.getLogger(PdfConverter.class);
    private final HttpClient client;
    private URI pdfSvgSupportGeneratorUrl;
    private OIDCRequestContextHolder contextHolder;
    private RestOperations restTemplate;


    public PdfConverter(@Value("${FAMILIE_DOKUMENT_API_URL}") URI pdfSvgSupportGeneratorUrl,
                        OIDCRequestContextHolder contextHolder,
                        RestOperations restTemplate) {
        this.contextHolder = contextHolder;
        this.client = HttpClientUtil.create();
        this.pdfSvgSupportGeneratorUrl = pdfSvgSupportGeneratorUrl;
        this.restTemplate = restTemplate;
    }

    byte[] genererPdfMedRestTemplate(byte[] bytes) {
        HttpHeaders headers = new HttpHeaders();
        URI dokumentUri = UriComponentsBuilder.fromUri(pdfSvgSupportGeneratorUrl).path("/html-til-pdf").build().toUri();
        log.info("post uri {}", dokumentUri);
        try {
            ResponseEntity<byte[]> response =
                    restTemplate.exchange(dokumentUri,
                                          HttpMethod.POST,
                                          new HttpEntity<>(new String(bytes, StandardCharsets.UTF_8), headers),
                                          byte[].class);
            log.info("Generer Pdf med familie-dokument: {}", response.getStatusCode().toString());
            return response.getBody();
        } catch (Exception e) {
            throw new InnsendingException("Feil med å genere Pdf med familie-dokument: "+ e.getMessage());
        }
    }

    byte[] genererPdf(byte[] bytes) {
        URI dokumentUri = URI.create(pdfSvgSupportGeneratorUrl + "html-til-pdf");
        log.info("post " + dokumentUri);
        try {
            var request = HttpClientUtil.createRequest(TokenHelper.generateAuthorizationHeaderValueForLoggedInUser(contextHolder))
                                        .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.TEXT_HTML_VALUE + "; charset=utf-8")
                                        .uri(URI.create(pdfSvgSupportGeneratorUrl + "html-til-pdf"))
                                        .POST(HttpRequest.BodyPublishers.ofString(new String(bytes, StandardCharsets.UTF_8)))
                                        .build();
            log.info("host "+ request.headers().map().get("Host").get(0));
            log.info("uri "+ request.uri());
            var response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (!HttpStatus.Series.SUCCESSFUL.equals(HttpStatus.Series.resolve(response.statusCode()))) {
                throw new InnsendingException("Response fra pdf-generator: " + response.statusCode() + ". Response.entity: " +
                                              new String(response.body()));
            }

            log.info("Konvertert søknad til pdf");

            return response.body();
        } catch (JsonProcessingException e) {
            throw new InnsendingException("Feiler under konvertering av innsending til json. " + e.getMessage());
        } catch (InterruptedException e) {
            throw new InnsendingException("Timer ut under innsending. " + e.getMessage());
        } catch (IOException e) {
            throw new InnsendingException("Ukjent IO feil. " + e.getMessage());
        }
    }

}

