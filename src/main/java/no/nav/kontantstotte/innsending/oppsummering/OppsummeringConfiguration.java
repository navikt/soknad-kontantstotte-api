package no.nav.kontantstotte.innsending.oppsummering;

import no.nav.kontantstotte.innsending.oppsummering.html.OppsummeringHtmlConfiguration;
import no.nav.kontantstotte.innsending.oppsummering.html.OppsummeringHtmlGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import java.net.URI;

@Configuration
@Import(OppsummeringHtmlConfiguration.class)
public class OppsummeringConfiguration {

    @Bean
    public OppsummeringPdfGenerator oppsummeringService(
            OppsummeringHtmlGenerator oppsummeringHtmlGenerator,
            PdfConverter pdfConverter) {
        return new OppsummeringPdfGenerator(oppsummeringHtmlGenerator, pdfConverter);
    }

}
