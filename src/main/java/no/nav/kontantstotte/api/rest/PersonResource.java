package no.nav.kontantstotte.api.rest;

import no.nav.security.oidc.api.Unprotected;
import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URI;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("person")
@Unprotected
public class PersonResource {

    @Value("${apikeys.key:x-nav-apiKey}")
    private String key;

    @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}")
    private URI proxyServiceUri;

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}")
    private String proxyApiKey;

    public PersonResource() {}

    @GET
    public String hentPerson() {
        WebTarget target = ClientBuilder
                .newClient()
                .register(OidcClientRequestFilter.class)
                .target(proxyServiceUri);

        String response = target
                .path("person")
                .request()
                .header(key, proxyApiKey)
                .get(String.class);
        return response;
    }
}
