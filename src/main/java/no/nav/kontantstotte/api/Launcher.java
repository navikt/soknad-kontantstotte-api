package no.nav.kontantstotte.api;

import no.nav.kontantstotte.api.config.ApplicationConfig;
import no.nav.security.spring.oidc.validation.api.EnableOIDCTokenValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
//@SpringBootApplication
//@EnableOIDCTokenValidation(ignore = "org.springframework")
public class Launcher extends SpringBootServletInitializer {

    public static void main(String... args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

}
