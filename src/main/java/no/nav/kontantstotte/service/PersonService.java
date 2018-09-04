package no.nav.kontantstotte.service;

import javax.ws.rs.client.Client;
import java.net.URI;

public class PersonService {

    private URI proxyServiceUri;

    private final Client client;

    public PersonService(Client client, URI proxyServiceUri) {
        this.client = client;
        this.proxyServiceUri = proxyServiceUri;
    }

    public String hentPerson() {

        return client.target(proxyServiceUri)
                .path("person")
                .request()
                .get(String.class);
    }
}
