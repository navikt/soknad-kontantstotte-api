package no.nav.kontantstotte.person;

import no.nav.kontantstotte.client.RestClientConfigration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import java.net.URI;

@Configuration
@Import(RestClientConfigration.class)
public class PersonConfiguration {

    @Bean
    public PersonService personServiceRetriever(
            @Named("proxyClient") Client client,
            @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI target) {

        return new PersonService(client, target);
    }
}
