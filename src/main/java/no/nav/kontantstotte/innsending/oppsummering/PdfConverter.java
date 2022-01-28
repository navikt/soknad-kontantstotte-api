package no.nav.kontantstotte.innsending.oppsummering;

import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.innsending.InnsendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

@Component
class PdfConverter {

    private static final Logger log = LoggerFactory.getLogger(PdfConverter.class);
    private final HttpClient client;
    private URI pdfSvgSupportGeneratorUrl;
    private RestOperations restTemplate;


    public PdfConverter(@Value("${FAMILIE_DOKUMENT_API_URL}") URI pdfSvgSupportGeneratorUrl,
                        RestOperations restTemplate) {
        this.client = HttpClientUtil.create();
        this.pdfSvgSupportGeneratorUrl = pdfSvgSupportGeneratorUrl;
        this.restTemplate = restTemplate;
    }

    byte[] genererPdf(byte[] bytes) {
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
            throw new InnsendingException("Feil med å genere Pdf med familie-dokument: " + e.getMessage());
        }
    }
}

