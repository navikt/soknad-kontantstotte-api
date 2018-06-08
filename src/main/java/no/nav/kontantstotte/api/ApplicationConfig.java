package no.nav.kontantstotte.api;

import no.nav.kontantstotte.api.config.RestConfiguration;
import no.nav.security.oidc.configuration.MultiIssuerConfiguraton;
import no.nav.security.oidc.configuration.OIDCResourceRetriever;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.filter.OIDCTokenValidationFilter;
import no.nav.security.spring.oidc.EnableOIDCTokenValidationConfiguration;
import no.nav.security.spring.oidc.MultiIssuerProperties;
import no.nav.security.spring.oidc.SpringOIDCRequestContextHolder;
import no.nav.security.spring.oidc.validation.api.EnableOIDCTokenValidation;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.jetty.ServletContextInitializerConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;

@SpringBootConfiguration
@ComponentScan({ "no.nav.security.oidc", "no.nav.kontantstotte" })
@EnableOIDCTokenValidation
public class ApplicationConfig {


//    private Environment env;

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

//    @Bean
//    public MultiIssuerConfiguraton multiIssuerConfiguration(MultiIssuerProperties issuerProperties, OIDCResourceRetriever resourceRetriever) {
//        return new MultiIssuerConfiguraton(issuerProperties.getIssuer(), resourceRetriever);
//    }
//
//    @Bean
//    public OIDCTokenValidationFilter tokenValidationFilter(MultiIssuerConfiguraton config, OIDCRequestContextHolder oidcRequestContextHolder) {
//        return new OIDCTokenValidationFilter(config, oidcRequestContextHolder);
//    }
//
//    @Bean
//    public FilterRegistrationBean<OIDCTokenValidationFilter> oidcTokenValidationFilterBean(OIDCTokenValidationFilter validationFilter) {
////        logger.info("Registering validation filter");
//        final FilterRegistrationBean<OIDCTokenValidationFilter> filterRegistration = new FilterRegistrationBean<>();
//        filterRegistration.setFilter(validationFilter);
//        filterRegistration.setMatchAfter(false);
//        filterRegistration
//                .setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC));
//        filterRegistration.setAsyncSupported(true);
//        filterRegistration.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return filterRegistration;
//    }
//
//    @Bean
//    OIDCRequestContextHolder oidcRequestContextHolder() {
//        return new SpringOIDCRequestContextHolder();
//    }
//
//
//
//    @Bean
//    public OIDCResourceRetriever oidcResourceRetriever(){
//        OIDCResourceRetriever resourceRetriever = new OIDCResourceRetriever();
//        resourceRetriever.setProxyUrl(getConfiguredProxy());
//        resourceRetriever.setUsePlainTextForHttps(Boolean.parseBoolean(env.getProperty("https.plaintext", "false")));
//        return resourceRetriever;
//    }
//
//
//    private URL getConfiguredProxy() {
//        String proxyParameterName = env.getProperty("http.proxy.parametername", "http.proxy");
//        String proxyconfig = env.getProperty(proxyParameterName);
//        URL proxy = null;
//        if(proxyconfig != null && proxyconfig.trim().length() > 0) {
////            log.info("Proxy configuration found [" + proxyParameterName +"] was " + proxyconfig);
//            try {
//                proxy = new URL(proxyconfig);
//            } catch (MalformedURLException e) {
//                throw new RuntimeException("config [" + proxyParameterName + "] is misconfigured: " + e, e);
//            }
//        } else {
////            logger.info("No proxy configuration found [" + proxyParameterName +"]");
//        }
//        return proxy;
//    }
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        this.env = environment;
//    }
}
