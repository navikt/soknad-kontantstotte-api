package no.nav.kontantstotte.api.rest;

import no.nav.security.oidc.api.ProtectedWithClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.net.URI;

@Component
@Path("/")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class InnloggingStatusResource {

    private final URI proxyServiceUri;

    private final Client client;

    public InnloggingStatusResource(
            @Named("kontantstotteProxyClient") Client client,
            @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI proxyServiceUri) {

        this.proxyServiceUri = proxyServiceUri;
        this.client = client;
    }

    /**
     * TODO Remove after frontend is updated
     * @deprecated remove after frontend is updated
     */
    @GET
    @Path("status/ping")
    @Deprecated
    public String ping() {
        return "pong";
    }

    @GET
    @Path("verify/loggedin")
    public Response verifyUserLoggedIn() {
        return Response.ok().build();
    }}