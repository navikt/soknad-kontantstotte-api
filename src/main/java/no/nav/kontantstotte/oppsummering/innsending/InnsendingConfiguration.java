package no.nav.kontantstotte.oppsummering.innsending;

import no.nav.kontantstotte.client.RestClientConfigration;
import no.nav.kontantstotte.oppsummering.InnsendingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import java.net.URI;
import java.util.List;

@Configuration
@Import(RestClientConfigration.class)
public class InnsendingConfiguration {

    @Bean
    public PdfService pdfServiceRetriever(
            @Named("client") Client client,
            @Value("${SOKNAD_PDF_GENERATOR_URL}") URI pdfGeneratorUrl,
            @Value("${SOKNAD_PDF_SVG_SUPPORT_GENERATOR_URL}") URI pdfSvgSupportGeneratorUrl
    ) {

        return new PdfService(client, pdfGeneratorUrl, pdfSvgSupportGeneratorUrl);
    }

    @Bean
    public OppsummeringService oppsummeringService(
            OppsummeringTransformer oppsummeringTransformer,
            PdfService pdfService) {
        return new OppsummeringService(pdfService, oppsummeringTransformer);
    }

    @Bean
    public InnsendingService innsendingServiceRetriever(
            @Named("proxyClient") Client client,
            @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI target,
            OppsummeringService oppsummeringService) {
        return new ArkivInnsendingService(client, target, oppsummeringService);
    }

    @Bean
    public OppsummeringTransformer oppsummeringTransformerRetriever() { return new OppsummeringTransformer(); }

}
