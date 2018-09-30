package no.nav.kontantstotte.oppsummering.innsending.v1;

import no.nav.kontantstotte.oppsummering.innsending.OppsummeringService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import java.net.URI;

@Configuration
public class OppsummeringV1Configuration {

    @Bean
    public OppsummeringService oppsummeringService(
            OppsummeringTransformer oppsummeringTransformer,
            PdfService pdfService) {
        return new NashornOppsummeringService(pdfService, oppsummeringTransformer);
    }


    @Bean
    public PdfService pdfServiceRetriever(
            @Named("client") Client client,
            @Value("${SOKNAD_PDF_GENERATOR_URL}") URI pdfGeneratorUrl,
            @Value("${SOKNAD_PDF_SVG_SUPPORT_GENERATOR_URL}") URI pdfSvgSupportGeneratorUrl
    ) {

        return new PdfService(client, pdfGeneratorUrl, pdfSvgSupportGeneratorUrl);
    }

    @Bean
    public OppsummeringTransformer oppsummeringTransformerRetriever() { return new OppsummeringTransformer(); }


}
