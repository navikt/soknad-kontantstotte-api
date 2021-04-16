package no.nav.kontantstotte.storage.attachment;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.client.TokenHelper;
import no.nav.kontantstotte.innsending.InnsendingException;
import no.nav.security.oidc.context.OIDCRequestContextHolder;

@Component
public class ImageConversionService {

    private static final Logger log = LoggerFactory.getLogger(ImageConversionService.class);

    private final HttpClient client;
    private final URI imageToPdfEndpointBaseUrl;
    private OIDCRequestContextHolder contextHolder;

    ImageConversionService(@Value("${SOKNAD_PDF_SVG_SUPPORT_GENERATOR_URL}") URI imageToPdfEndpointBaseUrl,
                           OIDCRequestContextHolder contextHolder) {
        this.contextHolder = contextHolder;
        this.client = HttpClientUtil.create();
        this.imageToPdfEndpointBaseUrl = imageToPdfEndpointBaseUrl;
    }

    public byte[] convert(byte[] bytes, Format detectedType) {
        try {
            log.info("Konverterer vedlegg på {} KB til PDF", bytes.length/1000);
            var request = HttpClientUtil.createRequest(TokenHelper.generateAuthorizationHeaderValueForLoggedInUser(contextHolder))
                    .header(HttpHeader.CONTENT_TYPE.asString(), detectedType.mimeType)
                    .uri(URI.create(imageToPdfEndpointBaseUrl + "v1/genpdf/image/kontantstotte"))
                    .POST(HttpRequest.BodyPublishers.ofByteArray(bytes))
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (!HttpStatus.Series.SUCCESSFUL.equals(HttpStatus.Series.resolve(response.statusCode()))) {
                throw new InnsendingException("Response fra pdf-generator: " +response.statusCode() + ". Response.entity: " + new String(response.body()));
            }

            log.info("Konvertert bilde({}) til pdf", detectedType.mimeType);

            return response.body();
        } catch (JsonProcessingException e) {
            throw new InnsendingException("Feiler under konvertering av innsending til json. " + e.getMessage());
        } catch (InterruptedException e) {
            throw new InnsendingException("Timer ut under innsending. " + e.getMessage());
        } catch (IOException e) {
            throw new InnsendingException("Ukjent IO feil i " + getClass().getName() + "." + e.getMessage());
        }
    }
}
