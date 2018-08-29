package no.nav.kontantstotte.service;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

public class ProxyHeaderRequestFilter implements ClientRequestFilter {

    private final String key;

    private final String proxyApiKey;

    public ProxyHeaderRequestFilter(String key, String proxyApiKey) {
        this.key = key;
        this.proxyApiKey = proxyApiKey;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {

        requestContext.getHeaders().putSingle(key, proxyApiKey);

    }

}
