package no.nav.kontantstotte.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EnableAutoConfiguration
//@SpringBootApplication
//@EnableOIDCTokenValidation(ignore = "org.springframework")
public class Launcher extends SpringBootServletInitializer {

    public static void main(String... args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

}
