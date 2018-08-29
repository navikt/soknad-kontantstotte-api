package no.nav.kontantstotte.config;

import no.nav.kontantstotte.service.InnsendingService;
import no.nav.kontantstotte.service.PdfService;
import no.nav.kontantstotte.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public PdfService pdfServiceRetriever() {
        return new PdfService();
    }

    @Bean
    public InnsendingService innsendingServiceRetriever() {
        return new InnsendingService();
    }

    @Bean
    public PersonService personServiceRetriever() {
        return new PersonService();
    }
}
