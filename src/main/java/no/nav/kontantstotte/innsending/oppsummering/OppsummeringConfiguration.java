package no.nav.kontantstotte.innsending.oppsummering;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.innsending.oppsummering.html.HtmlConverter;
import no.nav.kontantstotte.innsending.oppsummering.html.SoknadTilOppsummering;
import no.nav.kontantstotte.tekst.DefaultTekstProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import java.net.URI;

@Configuration
public class OppsummeringConfiguration {

    @Bean
    public OppsummeringPdfGenerator oppsummeringService(
            HtmlConverter htmlConverter,
            PdfConverter pdfConverter,
            SoknadTilOppsummering soknadTilOppsummering) {
        return new OppsummeringPdfGenerator(new DefaultTekstProvider(), htmlConverter, pdfConverter, soknadTilOppsummering);
    }

    @Bean
    public SoknadTilOppsummering soknadTilOppsummering(Unleash unleash){
        return new SoknadTilOppsummering(unleash);
    }


    @Bean
    public PdfConverter pdfServiceRetriever(
            @Named("client") Client client,
            @Value("${SOKNAD_PDF_SVG_SUPPORT_GENERATOR_URL}") URI pdfSvgSupportGeneratorUrl
    ) {

        return new PdfConverter(client, pdfSvgSupportGeneratorUrl);
    }

    @Bean
    public HtmlConverter htmlOppsummeringService(
            @Named("client") Client client,
            @Value("${SOKNAD_HTML_GENERATOR_URL}") URI htmlGeneratorUrl
    ) {
        return new HtmlConverter(client, htmlGeneratorUrl);
    }

}
