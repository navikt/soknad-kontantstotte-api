package no.nav.kontantstotte.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.config.DelayedShutdownHook;
import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableOIDCTokenValidation(ignore = "org.springframework")
public class Launcher {

    public static void main(String... args) {

        SpringApplication app = new SpringApplication(ApplicationConfig.class);
        app.setRegisterShutdownHook(false);
        ConfigurableApplicationContext applicationContext = app.run(args);
        Runtime.getRuntime().addShutdownHook(new DelayedShutdownHook(applicationContext));
    }


}
