package no.nav.kontantstotte.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.nav.sbl.rest.ClientLogFilter;
import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.ext.ContextResolver;

@Configuration
public class RestClientConfigration {

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_USERNAME}")
    private String kontantstotteProxyApiKeyUsername;

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}")
    private String kontantstotteProxyApiKeyPassword;

    @Value("${SOKNAD-KONTANTSTOTTE-API-TPS-PROXY_API_V1_INNSYN-APIKEY_USERNAME}")
    private String tpsProxyApiKeyUsername;

    @Value("${SOKNAD-KONTANTSTOTTE-API-TPS-PROXY_API_V1_INNSYN-APIKEY_PASSWORD}")
    private String tpsProxyApiKeyPassword;

    @Bean(name = "client")
    public Client client() {

        return ClientBuilder.newBuilder()
                .register(new ClientLogFilter(ClientLogFilter.ClientLogFilterConfig.builder()
                        .metricName("soknad-kontantstotte-api").build()))
                .register(OidcClientRequestFilter.class)
                .register(objectMapperResolver())
                .register(new LoggingFeature())
                .build();
    }

    @Bean(name = "kontantstotteProxyClient")
    public Client kontantstotteProxyClient() {

        return ClientBuilder.newBuilder()
                .register(new ClientLogFilter(ClientLogFilter.ClientLogFilterConfig.builder()
                        .metricName("soknad-kontantstotte-api").build()))
                .register(OidcClientRequestFilter.class)
                .register(objectMapperResolver())
                .register(new ProxyHeaderRequestFilter(kontantstotteProxyApiKeyUsername, kontantstotteProxyApiKeyPassword))
                .register(new LoggingFeature())
                .build();
    }

    @Bean(name = "tpsProxyClient")
    public Client tpsProxyClient() {

        return ClientBuilder.newBuilder()
                .register(new ClientLogFilter(ClientLogFilter.ClientLogFilterConfig.builder()
                        .metricName("soknad-kontantstotte-api").build()))
                .register(OidcClientRequestFilter.class)
                .register(objectMapperResolver())
                .register(new ProxyHeaderRequestFilter(tpsProxyApiKeyUsername, tpsProxyApiKeyPassword))
                .register(new LoggingFeature())
                .build();
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

}
