package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.client.RestClientConfigration;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringPdfGenerator;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringConfiguration;
import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.StorageConfiguration;
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
@Import({RestClientConfigration.class,
        OppsummeringConfiguration.class,
        StorageConfiguration.class
})
public class InnsendingConfiguration {

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_USERNAME}")
    private String kontantstotteProxyApiKeyUsername;

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}")
    private String kontantstotteProxyApiKeyPassword;

    @Bean
    public InnsendingService innsendingServiceRetriever(
            @Named("kontantstotteProxyClient") Client client,
            @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI target,
            OppsummeringPdfGenerator oppsummeringPdfGenerator,
            VedleggProvider vedleggProvider) {
        return new ArkivInnsendingService(client,
                target,
                oppsummeringPdfGenerator,
                vedleggProvider);
    }

    @Bean(name = "kontantstotteProxyClient")
    public Client kontantstotteProxyClient(ContextResolver<ObjectMapper> clientObjectMapperResolver) {

        return ClientBuilder.newBuilder()
                .register(new ClientLogFilter(ClientLogFilter.ClientLogFilterConfig.builder()
                        .metricName("soknad-kontantstotte-api").build()))
                .register(OidcClientRequestFilter.class)
                .register(clientObjectMapperResolver)
                .register(new LoggingFeature())
                .register((ClientRequestFilter) requestContext -> requestContext.getHeaders().putSingle(kontantstotteProxyApiKeyUsername, kontantstotteProxyApiKeyPassword))
                .build();
    }

    @Bean
    public VedleggProvider vedleggProvider(Storage storage) {
        return new VedleggProvider(storage);
    }

}
