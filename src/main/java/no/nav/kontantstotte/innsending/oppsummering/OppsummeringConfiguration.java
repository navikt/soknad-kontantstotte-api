package no.nav.kontantstotte.innsending.oppsummering;

import no.nav.kontantstotte.innsending.oppsummering.html.OppsummeringHtmlConfiguration;
import no.nav.kontantstotte.innsending.oppsummering.html.OppsummeringHtmlGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
