package no.nav.kontantstotte.innsending.oppsummering.html;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.innsending.InnsendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
class HtmlConverter {

    private static final Logger log = LoggerFactory.getLogger(HtmlConverter.class);
    private final String CONTENT_TYPE = "Content-Type";
    private final HttpClient client;
    private URI url;
    private ObjectMapper mapper;

    public HtmlConverter(@Value("${SOKNAD_HTML_GENERATOR_URL}") URI htmlGeneratorUrl,
                         ObjectMapper mapper) {
        this.mapper = mapper;
        this.client = HttpClientUtil.create();
        this.url = htmlGeneratorUrl;
    }

    public <T> byte[] genererHtml(T oppsummering) {

        HttpResponse<byte[]> response;
        URI generatorUri = URI.create(url + "generateHtml");
        log.info("generator uri " + generatorUri);
        try {
            HttpRequest request = HttpClientUtil.createRequest()
                                                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                .timeout(Duration.ofSeconds(10))
                                                .uri(URI.create(url + "generateHtml"))
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
            throw new InnsendingException("Response fra html-generator: " + response.statusCode() + ". Response.entity: " +
                                          new String(response.body()));
        }

        log.info("Konvertert søknad til html");

        return response.body();
    }
}

