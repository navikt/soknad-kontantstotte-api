package no.nav.kontantstotte.service;

import no.nav.sbl.rest.RestUtils;
import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;

@Configuration
public class ServiceConfiguration {

    @Value("${apikeys.key:x-nav-apiKey}")
    String key;

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}")
    String proxyApiKey;

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

    @Bean
    public PdfService pdfServiceRetriever(
            @Named("client") Client client,
            @Value("${SOKNAD_PDF_GENERATOR_URL}") URI pdfGeneratorTarget,
            @Value("${SOKNAD_PDF_GENERATOR_URL}") URI pdfgenTarget) {

        return new PdfService(client, pdfGeneratorTarget, pdfgenTarget);
    }

    @Bean
    public InnsendingService innsendingServiceRetriever(
            @Named("proxyClient") Client client,
            @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI target) {
        return new InnsendingService(client, target);
    }

    @Bean
    public PersonService personServiceRetriever(
            @Named("proxyClient") Client client,
            @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI target
    ) {
        return new PersonService(client, target);
    }
}
