package no.nav.kontantstotte.innsending.oppsummering.html;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

class HtmlConverter {
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

        return response.readEntity(byte[].class);
    }
}

