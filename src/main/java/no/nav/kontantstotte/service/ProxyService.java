package no.nav.kontantstotte.service;

import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.URI;

abstract class ProxyService {

    @Value("${apikeys.key:x-nav-apiKey}")
    String key;

    @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}")
    URI proxyServiceUri;

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}")
    String proxyApiKey;

    public WebTarget proxyTarget() {
        return ClientBuilder.newClient()
                .register(OidcClientRequestFilter.class)
                .target(proxyServiceUri);
    }
}
