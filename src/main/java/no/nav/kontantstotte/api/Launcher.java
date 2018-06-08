package no.nav.kontantstotte.api;

import no.nav.kontantstotte.api.config.RestConfiguration;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.spring.oidc.SpringOIDCRequestContextHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.jetty.ServletContextInitializerConfiguration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

//@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan({ "no.nav.security.oidc", "no.nav.kontantstotte" })
//@EnableOIDCTokenValidation(ignore = "org.springframework")
public class Launcher {

    public static void main(String... args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }


}
