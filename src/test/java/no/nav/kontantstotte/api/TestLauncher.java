package no.nav.kontantstotte.api;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringTestConfiguration;
import no.nav.kontantstotte.innsyn.domain.IInnsynClient;
import no.nav.kontantstotte.innsyn.domain.IInnsynService;
import no.nav.security.oidc.configuration.OIDCResourceRetriever;
import no.nav.security.oidc.test.support.FileResourceRetriever;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.BRUK_TPS_INTEGRASJON;
import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static org.mockito.Mockito.mock;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Import({ApplicationConfig.class, OppsummeringTestConfiguration.class})
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
        FakeUnleash fakeUnleash = new FakeUnleash();
        fakeUnleash.enable(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL);
        fakeUnleash.enable(BRUK_TPS_INTEGRASJON);
        return fakeUnleash;
    }

    @Bean
    @Primary
    public ResourceConfig proxyConfig() {
        return new TestRestConfiguration();
    }

    @Bean
    @Primary
    public IInnsynService innsynService() {
        return mock(IInnsynService.class);
    }

    @Bean
    @Primary
    public IInnsynClient innsynClient() {
        return mock(IInnsynClient.class);
    }

    @PostConstruct
    public void enableOriginHeaderForHttpClients() {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }

}
