package no.nav.kontantstotte.service;

import no.nav.kontantstotte.oppsummering.v2.SoknadOppsummering;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

public class OppsummeringService {
    private URI url;

    private final Client client;


    public OppsummeringService(Client client, URI oppsummeringServiceUrl) {
        this.client = client;
        this.url = oppsummeringServiceUrl;
    }

    public byte[] genererHtml(SoknadOppsummering oppsummering) {
        Response response = client
                .target(url)
                .path("convert")
                .request()
                .buildPost(Entity.entity(oppsummering, MediaType.APPLICATION_JSON_TYPE))
                .invoke();

        return response.readEntity(byte[].class);
    }

}
