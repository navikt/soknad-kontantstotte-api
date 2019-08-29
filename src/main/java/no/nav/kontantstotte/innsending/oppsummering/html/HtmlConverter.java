package no.nav.kontantstotte.innsending.oppsummering.html;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.client.TokenHelper;
import no.nav.kontantstotte.innsending.InnsendingException;
import no.nav.security.oidc.context.OIDCRequestContextHolder;

@Component
class HtmlConverter {
    private static final Logger log = LoggerFactory.getLogger(HtmlConverter.class);
    private final String CONTENT_TYPE = "Content-Type";
    private final HttpClient client;
    private URI url;
    private ObjectMapper mapper;
    private OIDCRequestContextHolder contextHolder;

    public HtmlConverter(@Value("${SOKNAD_HTML_GENERATOR_URL}") URI htmlGeneratorUrl,
                         ObjectMapper mapper,
                         OIDCRequestContextHolder contextHolder) {
        this.mapper = mapper;
        this.contextHolder = contextHolder;
        this.client = HttpClientUtil.create();
        this.url = htmlGeneratorUrl;
    }

    public byte[] genererHtml(SoknadOppsummering oppsummering) {

        HttpResponse<byte[]> response;
        try {
            HttpRequest request = HttpClientUtil.createRequest(TokenHelper.generateAuthorizationHeaderValueForLoggedInUser(contextHolder))
                    .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .timeout(Duration.ofSeconds(10))
                    .uri(URI.create(url + "/generateHtml"))
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(oppsummering)))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        } catch (JsonProcessingException e) {
            throw new InnsendingException("Feiler under konvertering av innsending til json. " + e.getMessage());
        } catch (InterruptedException e) {
            throw new InnsendingException("Timer ut under innsending. " + e.getMessage());
        } catch (IOException e) {
            throw new InnsendingException("Ukjent IO feil. " + e.getMessage());
        }

        if (response.statusCode() != HttpStatus.OK.value()) {
            throw new InnsendingException("Response fra html-generator: " + response.statusCode() + ". Response.entity: " + new String(response.body()));
        }

        log.info("Konvertert søknad til html");

        return response.body();
    }
}

