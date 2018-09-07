package no.nav.kontantstotte.api.rest;

import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.api.Unprotected;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
@Path("status")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class StatusResource {

    private final URI proxyServiceUri;

    private final Client client;

    public StatusResource(
            @Named("proxyClient") Client client,
            @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI proxyServiceUri) {

        this.proxyServiceUri = proxyServiceUri;
        this.client = client;
    }

    @GET
    @Path("ping")
    public String ping() {
        return "pong";
    }

    @GET
    @Path("selftest")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> selftest() {
        WebTarget target = client
                .target(proxyServiceUri);

        String response = target
                .path("status").path("ping")
                .request()
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