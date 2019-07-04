package no.nav.kontantstotte.api;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringTestConfiguration;
import no.nav.kontantstotte.innsyn.InnsynTestConfiguration;
import no.nav.kontantstotte.storage.s3.TestStorageConfiguration;
import no.nav.security.oidc.configuration.OIDCResourceRetriever;
import no.nav.security.oidc.test.support.FileResourceRetriever;
import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;
import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;

@ActiveProfiles("dev")
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Import({ApplicationConfig.class, OppsummeringTestConfiguration.class, InnsynTestConfiguration.class, TestStorageConfiguration.class, TokenGeneratorConfiguration.class})
@EnableOIDCTokenValidation(ignore = "org.springframework")
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
    OIDCResourceRetriever overrideOidcResourceRetriever() {
        return new FileResourceRetriever("/metadata.json", "/jwkset.json");
    }

    @Bean
    Unleash fakeUnleash() {
        return new FakeUnleash();
    }

    @PostConstruct
    public void enableOriginHeaderForHttpClients() {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }

}
