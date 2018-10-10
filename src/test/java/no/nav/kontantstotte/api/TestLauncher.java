package no.nav.kontantstotte.api;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.v2.HtmlOppsummeringService;
import no.nav.kontantstotte.oppsummering.innsending.v2.PdfGenService;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering;
import no.nav.security.oidc.configuration.OIDCResourceRetriever;
import no.nav.security.oidc.test.support.FileResourceRetriever;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.BRUK_PDFGEN;
import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_NY_OPPSUMMERING;
import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Import(ApplicationConfig.class)
public class TestLauncher {

    public static void main(String... args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

    /**
     * To be able to ovverride the oidc validation properties in
     * EnableOIDCTokenValidationConfiguration in oidc-spring-support
     */
    @Bean
    @Primary
    OIDCResourceRetriever overrideOidcResourceRetriever(){
        return new FileResourceRetriever("/metadata.json", "/jwkset.json");
    }

    @Bean
    @Primary
    PdfGenService pdfGenService() throws IOException {
        PdfGenService service = mock(PdfGenService.class);
        byte[] b = readFile("oppsummering.pdf");
        when(service.genererPdf(any())).thenReturn(b);
        return service;
    }


    @Bean
    @Primary
    HtmlOppsummeringService htmlOppsummeringService() throws IOException {
        HtmlOppsummeringService service = mock(HtmlOppsummeringService.class);
        byte[] b = readFile("oppsummering.html");
        when(service.genererHtml(any())).thenReturn(b);
        return service;
    }

    private byte[] readFile(String filename) throws IOException {
        File file = new File(getClass().getClassLoader().getResource(filename).getFile());
        RandomAccessFile f = new RandomAccessFile(file, "r");
        byte[] b = new byte[(int)f.length()];
        f.readFully(b);
        return b;
    }

    @Bean
    Unleash fakeUnleash() {
        FakeUnleash fakeUnleash = new FakeUnleash();
        fakeUnleash.enable(BRUK_PDFGEN);
        fakeUnleash.enable(KONTANTSTOTTE_NY_OPPSUMMERING);
        fakeUnleash.enable(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL);

        return fakeUnleash;
    }

    @Bean
    @Primary
    ServletRegistrationBean<?> jerseyServletRegistration() {

        ServletRegistrationBean<?> jerseyServletRegistration = new ServletRegistrationBean<>(new ServletContainer());

        jerseyServletRegistration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, TestRestConfiguration.class.getName());

        return jerseyServletRegistration;
    }
}
