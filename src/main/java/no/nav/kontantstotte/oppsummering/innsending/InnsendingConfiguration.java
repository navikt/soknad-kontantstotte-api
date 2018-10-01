package no.nav.kontantstotte.oppsummering.innsending;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.client.RestClientConfigration;
import no.nav.kontantstotte.oppsummering.InnsendingService;
import no.nav.kontantstotte.oppsummering.innsending.v1.OppsummeringV1Configuration;
import no.nav.kontantstotte.oppsummering.innsending.v2.OppsummeringV2Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import java.net.URI;

@Configuration
@Import({RestClientConfigration.class,
        OppsummeringV1Configuration.class,
        OppsummeringV2Configuration.class
})
public class InnsendingConfiguration {


    @Bean
    public InnsendingService innsendingServiceRetriever(
            @Named("proxyClient") Client client,
            @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI target,
            @Named("v1") OppsummeringGenerator oppsummeringGeneratorV1,
            @Named("v2") OppsummeringGenerator oppsummeringGeneratorV2,
            Unleash unleash) {
        return new ArkivInnsendingService(client,
                target,
                oppsummeringGeneratorV1,
                oppsummeringGeneratorV2,
                unleash);
    }

}
