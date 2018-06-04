package no.nav.kontantstotte.api;

import no.nav.kontantstotte.api.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Launcher extends SpringBootServletInitializer {

    public static void main(String... args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

}
