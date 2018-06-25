package no.nav.kontantstotte.api.rest;

import no.nav.security.oidc.api.ProtectedWithClaims;
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
import java.util.HashMap;
import java.util.Map;

@Component
@Path("status")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class StatusResource {

    @Value("${apikeys.key:x-nav-apiKey}")
    private String key;

    @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}")
    private URI proxyServiceUri;

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}")
    private String proxyApiKey;

    @GET
    @Path("ping")
    public String ping() {
        return "pong";
    }

    @GET
    @Path("selftest")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> selftest() {
        WebTarget target = ClientBuilder
                .newClient()
                .register(OidcClientRequestFilter.class)
                .target(proxyServiceUri);

        String response = target
                .path("status").path("ping")
                .request()
                .header(key, proxyApiKey)
                .get(String.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("self", ping());
        map.put(target.getUri().toString(), response);
        return map;


    }

    @GET
    @Path("isAlive")
    @Unprotected
    public String isAlive() {
        return "Ok";
    }
}

// https://github.com/navikt/token-support/tree/master/oidc-spring-test