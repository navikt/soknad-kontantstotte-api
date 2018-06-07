package no.nav.kontantstotte.api.config;

import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.spring.oidc.SpringOIDCRequestContextHolder;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.jetty.ServletContextInitializerConfiguration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@ComponentScan({ "no.nav.security.oidc", "no.nav.kontantstotte" })
@Configuration
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

//    @Bean
//    public void something() {
//        AnnotationConfigWebApplicationContext
//        new ContextLoaderListener(this);
//    }
//    static class ServletConfig implements ServletContextInitializer {
//
//        @Override
//        public void onStartup(ServletContext servletContext) throws ServletException {
//            servletContext.
//        }
//    }

    @Bean
    ServletRegistrationBean<?> jerseyServlet() {

        ServletRegistrationBean<?> jerseyServletRegistration = new ServletRegistrationBean<>(new ServletContainer());

        jerseyServletRegistration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, RestConfiguration.class.getName());

        return jerseyServletRegistration;
    }

    @Bean
    OIDCRequestContextHolder oidcRequestContextHolder() {
        return new SpringOIDCRequestContextHolder();
    }
}
