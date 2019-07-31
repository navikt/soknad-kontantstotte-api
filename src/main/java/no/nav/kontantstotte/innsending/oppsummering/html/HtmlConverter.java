package no.nav.kontantstotte.innsending.oppsummering.html;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.client.TokenHelper;
import no.nav.kontantstotte.innsending.InnsendingException;
import no.nav.security.oidc.context.OIDCRequestContextHolder;

@Component
class HtmlConverter {
    private static final Logger log = LoggerFactory.getLogger(HtmlConverter.class);
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
                    .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.APPLICATION_JSON)
                    .timeout(Duration.ofSeconds(10))
                    .uri(UriBuilder.fromUri(url).path("generateHtml").build())
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

        if (!SUCCESSFUL.equals(Response.Status.Family.familyOf(response.statusCode()))) {
            throw new InnsendingException("Response fra html-generator: " + Response.Status.fromStatusCode(response.statusCode()) + ". Response.entity: " + new String(response.body()));
        }

        log.info("Konvertert søknad til html");

        return response.body();
    }
}

