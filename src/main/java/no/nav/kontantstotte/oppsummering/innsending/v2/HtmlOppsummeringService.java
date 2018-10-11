package no.nav.kontantstotte.oppsummering.innsending.v2;

import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadOppsummering;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

public class HtmlOppsummeringService {
    private URI url;

    private final Client client;


    public HtmlOppsummeringService(Client client, URI url) {
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

