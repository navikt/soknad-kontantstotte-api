package no.nav.kontantstotte.client;

import no.nav.sbl.rest.RestUtils;
import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Configuration
public class RestClientConfigration {

    @Value("${apikeys.key:x-nav-apiKey}")
    private String key;

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}")
    private String proxyApiKey;

    @Bean(name = "client")
    public Client client() {
        ClientConfig clientConfig = RestUtils
                .createClientConfig()
                .register(OidcClientRequestFilter.class);

        return ClientBuilder.newBuilder().withConfig(clientConfig).build();
    }

    @Bean(name = "proxyClient")
    public Client proxyClient(ProxyHeaderRequestFilter proxyHeaderRequestFilter) {

        ClientConfig clientConfig = RestUtils
                .createClientConfig()
                .register(OidcClientRequestFilter.class)
                .register(proxyHeaderRequestFilter);

        return ClientBuilder.newBuilder().withConfig(clientConfig).build();
    }

    @Bean
    public ProxyHeaderRequestFilter proxyHeaderRequestFilter() {

        return new ProxyHeaderRequestFilter(key, proxyApiKey);
    }

}
