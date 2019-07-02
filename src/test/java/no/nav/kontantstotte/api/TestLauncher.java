package no.nav.kontantstotte.api;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringTestConfiguration;
import no.nav.kontantstotte.innsyn.InnsynTestConfiguration;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.storage.s3.TestStorageConfiguration;
import no.nav.security.oidc.configuration.OIDCResourceRetriever;
import no.nav.security.oidc.test.support.FileResourceRetriever;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

import static org.mockito.Mockito.mock;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Import({ApplicationConfig.class, OppsummeringTestConfiguration.class, InnsynTestConfiguration.class, TestStorageConfiguration.class})
public class TestLauncher {

    public static void main(String... args) {
        new SpringApplicationBuilder(ApplicationConfig.class)
                .web(WebApplicationType.SERVLET)
                .build()
                .run(args);
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
        return fakeUnleash;
    }

    @Bean
    @Primary
    public ResourceConfig proxyConfig() {
        return new TestRestConfiguration();
    }

    @Bean
    @Profile("!mockgen-tps")
    @Primary
    public InnsynService innsynServiceClient() {
        return mock(InnsynService.class);
    }

    @PostConstruct
    public void enableOriginHeaderForHttpClients() {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }

}
