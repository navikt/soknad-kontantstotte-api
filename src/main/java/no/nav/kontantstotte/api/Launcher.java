package no.nav.kontantstotte.api;

import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.config.DelayedShutdownHook;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableJwtTokenValidation
public class Launcher {

    public static void main(String... args) {

        SpringApplication app = new SpringApplication(ApplicationConfig.class);
        app.setRegisterShutdownHook(false);
        ConfigurableApplicationContext applicationContext = app.run(args);
        Runtime.getRuntime().addShutdownHook(new DelayedShutdownHook(applicationContext));
    }


}
