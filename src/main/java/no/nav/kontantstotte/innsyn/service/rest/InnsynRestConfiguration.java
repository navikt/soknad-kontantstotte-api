package no.nav.kontantstotte.innsyn.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.client.RestClientConfigration;
import no.nav.kontantstotte.innsyn.domain.IInnsynClient;
import no.nav.sbl.rest.ClientLogFilter;
import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.ContextResolver;
import java.net.URI;

@Configuration
@Import(RestClientConfigration.class)
public class InnsynRestConfiguration {

    @Value("${SOKNAD-KONTANTSTOTTE-API-TPS-PROXY_API_V1_INNSYN-APIKEY_USERNAME}")
    private String tpsProxyApiKeyUsername;

    @Value("${SOKNAD-KONTANTSTOTTE-API-TPS-PROXY_API_V1_INNSYN-APIKEY_PASSWORD}")
    private String tpsProxyApiKeyPassword;

    @Bean
    public no.nav.kontantstotte.innsyn.domain.IInnsynService innsynServiceRest(
            @Named("tpsProxyClient") Client client,
            @Value("${TPS-PROXY_API_V1_INNSYN_URL}") URI tpsInnsynServiceUri) {
        return new InnsynService(client, tpsInnsynServiceUri);
    }
    @Bean
    public InnsynRestHealthIndicator innsynServiceHealthIndicator(IInnsynClient innsynClient) {
        return new InnsynRestHealthIndicator(innsynClient);
    }

    @Bean(name = "tpsProxyClient")
    public Client tpsProxyClient(ContextResolver<ObjectMapper> clientObjectMapperResolver) {
        return ClientBuilder.newBuilder()
                .register(new ClientLogFilter(ClientLogFilter.ClientLogFilterConfig.builder()
                        .metricName("soknad-kontantstotte-api").build()))
                .register(OidcClientRequestFilter.class)
                .register(clientObjectMapperResolver)
                .register(new LoggingFeature())
                .register((ClientRequestFilter) requestContext -> requestContext.getHeaders().putSingle(tpsProxyApiKeyUsername, tpsProxyApiKeyPassword))
                .build();
    }

}
