package no.nav.kontantstotte.service;

import no.nav.kontantstotte.oppsummering.SoknadDto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

public class InnsendingService {

    private URI proxyServiceUri;

    private final Client client;

    public InnsendingService(Client client, URI proxyServiceUri) {
        this.client = client;
        this.proxyServiceUri = proxyServiceUri;
    }

    public Response sendInnSoknad(SoknadDto soknadDto) {

        return client.target(proxyServiceUri)
                .path("soknad")
                .request()
                .buildPost(Entity.entity(soknadDto, MediaType.APPLICATION_JSON))
                .invoke();
    }
}
