package no.nav.kontantstotte.oppsummering.innsending.v2;

import no.nav.kontantstotte.oppsummering.innsending.OppsummeringService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import java.net.URI;

@Configuration
public class OppsummeringV2Configuration {

    @Bean("v2")
    public OppsummeringService oppsummeringService(
            HtmlOppsummeringService htmlOppsummeringService,
            PdfGenService pdfService) {
        return new NodeOppsummeringService(pdfService);
    }


    @Bean
    public PdfGenService pdfServiceRetriever(
            @Named("client") Client client,
            @Value("${SOKNAD_PDF_SVG_SUPPORT_GENERATOR_URL}") URI pdfSvgSupportGeneratorUrl
    ) {

        return new PdfGenService(client, pdfSvgSupportGeneratorUrl);
    }

    @Bean
    public HtmlOppsummeringService htmlOppsummeringService(
            @Named("client") Client client,
            @Value("${SOKNAD_OPPSUMMERING_GENERATOR_URL}") URI pdfSvgSupportGeneratorUrl
    ) {

        return new HtmlOppsummeringService(client, pdfSvgSupportGeneratorUrl);
    }

}
