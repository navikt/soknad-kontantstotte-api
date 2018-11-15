package no.nav.kontantstotte.innsending.oppsummering.html;

import no.nav.kontantstotte.innsending.InnsendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

class HtmlConverter {
    private static final Logger log = LoggerFactory.getLogger(HtmlConverter.class);

    private URI url;

    private final Client client;

    public HtmlConverter(Client client, URI url) {
        this.client = client;
        this.url = url;

    }

    public byte[] genererHtml(SoknadOppsummering oppsummering) {

        Response response = client
                .target(url)
                .path("generateHtml")
                .request()
                .buildPost(Entity.entity(oppsummering, MediaType.APPLICATION_JSON_TYPE))
                .invoke();

        if(!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            throw new InnsendingException("Response fra html-generator: "+ response.getStatus() + ". Response.entity: " + response.readEntity(String.class));
        }

        log.info("Konvertert søknad til html");

        return response.readEntity(byte[].class);
    }
}

