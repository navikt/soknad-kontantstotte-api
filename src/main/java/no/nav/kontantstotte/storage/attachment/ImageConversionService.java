package no.nav.kontantstotte.storage.attachment;

import no.nav.kontantstotte.innsending.InnsendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

class ImageConversionService {

    private static final Logger log = LoggerFactory.getLogger(ImageConversionService.class);

    private final Client client;
    private final URI imageToPdfEndpointBaseUrl;

    ImageConversionService(Client client, URI imageToPdfEndpointBaseUrl) {

        this.client = client;
        this.imageToPdfEndpointBaseUrl = imageToPdfEndpointBaseUrl;
    }

    byte[] convert(byte[] bytes, Format detectedType) {
        Response response = client
                .target(imageToPdfEndpointBaseUrl)
                .path("v1/genpdf/image/kontantstotte")
                .request()
                .buildPost(Entity.entity(bytes, detectedType.mimeType))
                .invoke();

        if(!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            throw new InnsendingException("Response fra pdf-generator: "+ response.getStatus() + ". Response.entity: " + response.readEntity(String.class));
        }

        log.info("Konvertert bilde({}) til pdf  ", detectedType.mimeType);

        return response.readEntity(byte[].class);
    }
}
