package no.nav.kontantstotte.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.nav.sbl.rest.RestUtils;
import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.ext.ContextResolver;

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
                .register(OidcClientRequestFilter.class)
                .register(objectMapperResolver())
                .register(new LoggingFeature());

        return ClientBuilder.newBuilder().withConfig(clientConfig).build();
    }

    private ContextResolver<ObjectMapper> objectMapperResolver() {
        return new ContextResolver<ObjectMapper>() {
            @Override
            public ObjectMapper getContext(Class<?> type) {
                return new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }
        };
    }

    @Bean(name = "proxyClient")
    public Client proxyClient(ProxyHeaderRequestFilter proxyHeaderRequestFilter) {

        ClientConfig clientConfig = RestUtils
                .createClientConfig()
                .register(objectMapperResolver())
                .register(OidcClientRequestFilter.class)
                .register(proxyHeaderRequestFilter)
                .register(new LoggingFeature());

        return ClientBuilder.newBuilder().withConfig(clientConfig).build();
    }

    @Bean
    public ProxyHeaderRequestFilter proxyHeaderRequestFilter() {

        return new ProxyHeaderRequestFilter(key, proxyApiKey);
    }

}
