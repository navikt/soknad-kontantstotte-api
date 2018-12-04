package no.nav.kontantstotte.config;

import no.nav.kontantstotte.api.filter.SecurityHttpHeaderFilter;
import no.nav.kontantstotte.config.toggle.FeatureToggleConfig;
import no.nav.kontantstotte.innsending.InnsendingConfiguration;
import no.nav.kontantstotte.innsyn.service.rest.InnsynRestConfiguration;
import no.nav.log.LogFilter;
import no.nav.security.oidc.configuration.MultiIssuerConfiguraton;
import no.nav.security.oidc.configuration.OIDCResourceRetriever;
import no.nav.security.oidc.jaxrs.servlet.JaxrsOIDCTokenValidationFilter;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.DispatcherType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;

@SpringBootConfiguration
@Import({FeatureToggleConfig.class, InnsendingConfiguration.class, InnsynRestConfiguration.class})
@ComponentScan({"no.nav.kontantstotte.api"})
@EnableConfigurationProperties(MultiIssuerProperties.class)
public class ApplicationConfig implements EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);
    private int WAIT_BEFORE_SHUTDOWN = 5000;

    private Environment env;

    @Bean
    ServletWebServerFactory servletWebServerFactory() {

        JettyServletWebServerFactory serverFactory = new JettyServletWebServerFactory();

        serverFactory.setPort(8080);
        serverFactory.addServerCustomizers((server) -> {
            StatisticsHandler handler = new StatisticsHandler();
            handler.setHandler(server.getHandler());
            server.setHandler(handler);
            server.setStopTimeout(WAIT_BEFORE_SHUTDOWN);
                }
        );

        return serverFactory;
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }


    @Bean
    public ResourceConfig apiConfig() {
        return new RestConfiguration();
    }

    @Bean
    public MultiIssuerConfiguraton multiIssuerConfiguration(MultiIssuerProperties issuerProperties, OIDCResourceRetriever resourceRetriever) {
        return new MultiIssuerConfiguraton(issuerProperties.getIssuer(), resourceRetriever);
    }

    @Bean
    public JaxrsOIDCTokenValidationFilter tokenValidationFilter(MultiIssuerConfiguraton config) {
        return new JaxrsOIDCTokenValidationFilter(config);
    }

    @Bean
    public FilterRegistrationBean<LogFilter> logFilter() {
        log.info("Registering LogFilter filter");
        final FilterRegistrationBean<LogFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new LogFilter());
        filterRegistration.setOrder(1);
        return filterRegistration;
    }

    @Bean
    public FilterRegistrationBean<SecurityHttpHeaderFilter> securityHttpHeaderFilterBean() {
        return new FilterRegistrationBean<>(new SecurityHttpHeaderFilter());
    }

    @Bean
    public FilterRegistrationBean<JaxrsOIDCTokenValidationFilter> oidcTokenValidationFilterBean(JaxrsOIDCTokenValidationFilter validationFilter) {
        log.info("Registering validation filter");
        final FilterRegistrationBean<JaxrsOIDCTokenValidationFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(validationFilter);
        filterRegistration.setMatchAfter(false);
        filterRegistration
                .setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC));
        filterRegistration.setAsyncSupported(true);
        filterRegistration.setOrder(2);
        return filterRegistration;
    }

    @Bean
    public OIDCResourceRetriever oidcResourceRetriever() {
        OIDCResourceRetriever resourceRetriever = new OIDCResourceRetriever();
        resourceRetriever.setProxyUrl(getConfiguredProxy());
        resourceRetriever.setUsePlainTextForHttps(Boolean.parseBoolean(env.getProperty("https.plaintext", "false")));
        return resourceRetriever;
    }

    private URL getConfiguredProxy() {
        String proxyParameterName = env.getProperty("http.proxy.parametername", "http.proxy");
        String proxyconfig = env.getProperty(proxyParameterName);
        URL proxy = null;
        if (proxyconfig.trim().length() > 0) {
            log.info("Proxy configuration found [" + proxyParameterName + "] was " + proxyconfig);
            try {
                proxy = new URL(proxyconfig);
            } catch (MalformedURLException e) {
                throw new RuntimeException("config [" + proxyParameterName + "] is misconfigured: " + e, e);
            }
        } else {
            log.info("No proxy configuration found [" + proxyParameterName + "]");
        }
        return proxy;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }


}
