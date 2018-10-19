package no.nav.kontantstotte.person.service.rest;

import no.nav.kontantstotte.client.RestClientConfigration;
import no.nav.kontantstotte.person.domain.PersonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import java.net.URI;

@Configuration
@Import(RestClientConfigration.class)
public class PersonRestConfiguration {

    @Bean
    public PersonService personServiceRest(
            @Named("client")Client client,
            @Value("${PERSONINFO_V1_URL}") URI personServiceUri) {
        return new PersonServiceClient(client, personServiceUri);
    }

    @Bean
    public PersonRestHealthIndicator personServiceHealthIndicator(PersonService personService) {
        return new PersonRestHealthIndicator(personService);
    }
}
