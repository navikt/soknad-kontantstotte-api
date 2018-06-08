package no.nav.kontantstotte.api;

import no.nav.kontantstotte.api.config.RestConfiguration;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.spring.oidc.SpringOIDCRequestContextHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.jetty.ServletContextInitializerConfiguration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootConfiguration
@ComponentScan({ "no.nav.security.oidc", "no.nav.kontantstotte" })
public class ApplicationConfig {

    @Bean
    ServletWebServerFactory servletWebServerFactory() {

        JettyServletWebServerFactory serverFactory = new JettyServletWebServerFactory();

        serverFactory.setPort(8080);
        serverFactory.addConfigurations(new ServletContextInitializerConfiguration(
                new ServletContextInitializer() {
                    @Override
                    public void onStartup(ServletContext servletContext) throws ServletException {
                        servletContext.addListener(RequestContextListener.class);
                    }
                }
        ));

        return serverFactory;
    }

    @Bean
    ServletRegistrationBean<?> jerseyServletRegistration() {

        ServletRegistrationBean<?> jerseyServletRegistration = new ServletRegistrationBean<>(new ServletContainer());

        jerseyServletRegistration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, RestConfiguration.class.getName());

        return jerseyServletRegistration;
    }

    @Bean
    OIDCRequestContextHolder oidcRequestContextHolder() {
        return new SpringOIDCRequestContextHolder();
    }
}
