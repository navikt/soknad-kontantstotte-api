package no.nav.kontantstotte.innsyn.service.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.kontantstotte.client.RestClientConfiguration;
import no.nav.kontantstotte.innsyn.domain.InnsynService;

@Configuration
@Import(RestClientConfiguration.class)
public class InnsynRestConfiguration {

    @Bean
    public InnsynRestHealthIndicator innsynServiceHealthIndicator(InnsynService innsynServiceClient) {
        return new InnsynRestHealthIndicator(innsynServiceClient);
    }

}
