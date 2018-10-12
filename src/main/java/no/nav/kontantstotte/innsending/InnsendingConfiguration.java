package no.nav.kontantstotte.innsending;

import no.nav.kontantstotte.client.RestClientConfigration;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringPdfGenerator;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import java.net.URI;

@Configuration
@Import({RestClientConfigration.class,
        OppsummeringConfiguration.class
})
public class InnsendingConfiguration {


    @Bean
    public InnsendingService innsendingServiceRetriever(
            @Named("proxyClient") Client client,
            @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI target,
            OppsummeringPdfGenerator oppsummeringPdfGenerator) {
        return new ArkivInnsendingService(client,
                target,
                oppsummeringPdfGenerator);
    }

}
