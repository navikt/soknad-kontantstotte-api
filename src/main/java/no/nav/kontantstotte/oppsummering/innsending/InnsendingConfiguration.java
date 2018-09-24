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

@Configuration
@Import(RestClientConfigration.class)
public class InnsendingConfiguration {

    @Bean
    public PdfService pdfServiceRetriever(
            @Named("client") Client client,
            @Value("${SOKNAD_PDF_GENERATOR_URL}") URI pdfGeneratorUrl,
            @Value("${SOKNAD_PDF_SVG_SUPPORT_GENERATOR_URL}") URI pdfSvgSupportGeneratorUrl,
            OppsummeringTransformer oppsummeringTransformer) {

        return new PdfService(client, pdfGeneratorUrl, pdfSvgSupportGeneratorUrl, oppsummeringTransformer);
    }

    @Bean
    public InnsendingService innsendingServiceRetriever(
            @Named("proxyClient") Client client,
            @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI target,
            PdfService pdfService) {
        return new FssInnsendingService(client, target, pdfService);
    }

    @Bean
    public OppsummeringTransformer oppsummeringTransformerRetriever() { return new OppsummeringTransformer(); }

}
