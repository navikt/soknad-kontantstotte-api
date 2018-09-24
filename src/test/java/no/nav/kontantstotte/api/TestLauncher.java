package no.nav.kontantstotte.api;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.security.oidc.configuration.OIDCResourceRetriever;
import no.nav.security.oidc.test.support.FileResourceRetriever;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import static no.nav.kontantstotte.service.PdfService.BRUK_PDFGEN;

@SpringBootApplication
@ComponentScan({ "no.nav.kontantstotte.api" })
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
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
    Unleash fakeUnleash() {
        FakeUnleash unleash = new FakeUnleash();
        unleash.enable( BRUK_PDFGEN );
        return unleash;
    }

    @Bean
    @Primary
    ServletRegistrationBean<?> jerseyServletRegistration() {

        ServletRegistrationBean<?> jerseyServletRegistration = new ServletRegistrationBean<>(new ServletContainer());

        jerseyServletRegistration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, TestRestConfiguration.class.getName());

        return jerseyServletRegistration;
    }
}
