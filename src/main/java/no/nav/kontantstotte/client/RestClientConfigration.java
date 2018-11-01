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

    @Bean(name = "client")
    public Client client(ContextResolver<ObjectMapper> clientObjectMapperResolver) {

        return ClientBuilder.newBuilder()
                .register(new ClientLogFilter(ClientLogFilter.ClientLogFilterConfig.builder()
                        .metricName("soknad-kontantstotte-api").build()))
                .register(OidcClientRequestFilter.class)
                .register(clientObjectMapperResolver)
                .register(new LoggingFeature())
                .build();
    }

    @Bean
    public ContextResolver<ObjectMapper> clientObjectMapperResolver() {
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
