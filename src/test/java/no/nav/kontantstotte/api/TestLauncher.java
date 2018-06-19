package no.nav.kontantstotte.api;

import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.security.spring.oidc.test.TokenGeneratorConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ComponentScan({ "no.nav.kontantstotte.api" })
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@Import(TokenGeneratorConfiguration.class)
public class TestLauncher {

    public static void main(String... args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

}
