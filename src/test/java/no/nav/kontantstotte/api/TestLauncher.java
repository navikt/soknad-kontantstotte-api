package no.nav.kontantstotte.api;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringTestConfiguration;
import no.nav.kontantstotte.innsyn.InnsynTestConfiguration;
import no.nav.kontantstotte.storage.s3.TestStorageConfiguration;
import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;
import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Import({ApplicationConfig.class, OppsummeringTestConfiguration.class, InnsynTestConfiguration.class, TestStorageConfiguration.class, TokenGeneratorConfiguration.class})
@EnableOIDCTokenValidation(ignore = "org.springframework")
public class TestLauncher {

    public static void main(String... args) {
        SpringApplication app = new SpringApplicationBuilder(ApplicationConfig.class)
                //.profiles("dev", "mockgen-tps", "mockgen-pdf")
                .build();
        app.run(args);
    }

    @Bean
    Unleash fakeUnleash() {
        FakeUnleash unleash = new FakeUnleash();
        return unleash;
    }

    @PostConstruct
    public void enableOriginHeaderForHttpClients() {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }

}
