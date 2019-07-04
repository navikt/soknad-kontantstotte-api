package no.nav.kontantstotte.innsending.oppsummering;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import no.nav.kontantstotte.client.TokenHelper;
import no.nav.kontantstotte.innsending.InnsendingException;
import no.nav.security.oidc.context.OIDCRequestContextHolder;

@Component
class PdfConverter {

    private static final Logger log = LoggerFactory.getLogger(PdfConverter.class);
    private final HttpClient client;
    private URI pdfSvgSupportGeneratorUrl;
    private OIDCRequestContextHolder contextHolder;


    public PdfConverter(@Value("${SOKNAD_PDF_SVG_SUPPORT_GENERATOR_URL}") URI pdfSvgSupportGeneratorUrl,
                        OIDCRequestContextHolder contextHolder) {
        this.contextHolder = contextHolder;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.pdfSvgSupportGeneratorUrl = pdfSvgSupportGeneratorUrl;
    }

    byte[] genererPdf(byte[] bytes) {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(pdfSvgSupportGeneratorUrl.resolve("v1/genpdf/html/kontantstotte"))
                    .POST(HttpRequest.BodyPublishers.ofByteArray(bytes))
                    .header(HttpHeader.AUTHORIZATION.asString(), TokenHelper.generatAuthorizationHeaderValueForLoggedInUser(contextHolder))
                    .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.TEXT_HTML)
                    .timeout(Duration.ofMinutes(2))
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (!SUCCESSFUL.equals(Response.Status.Family.familyOf(response.statusCode()))) {
                throw new InnsendingException("Response fra pdf-generator: " + Response.Status.fromStatusCode(response.statusCode()) + ". Response.entity: " + new String(response.body()));
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

