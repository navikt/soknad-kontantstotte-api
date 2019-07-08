package no.nav.kontantstotte.config;

import no.nav.kontantstotte.api.filter.SecurityHttpHeaderFilter;
import no.nav.kontantstotte.config.toggle.FeatureToggleConfig;
import no.nav.kontantstotte.innsending.InnsendingConfiguration;
import no.nav.kontantstotte.innsyn.service.rest.InnsynRestConfiguration;
import no.nav.kontantstotte.storage.attachment.AttachmentConfiguration;
import no.nav.kontantstotte.storage.encryption.EncryptedStorageConfiguration;
import no.nav.log.LogFilter;
import no.nav.security.spring.oidc.MultiIssuerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;

@SpringBootConfiguration
@Import({FeatureToggleConfig.class, InnsendingConfiguration.class, InnsynRestConfiguration.class, EncryptedStorageConfiguration.class, AttachmentConfiguration.class})
@ComponentScan({"no.nav.kontantstotte"})
@EnableConfigurationProperties(MultiIssuerProperties.class)
public class ApplicationConfig {

    private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {

        JettyServletWebServerFactory serverFactory = new JettyServletWebServerFactory();

        serverFactory.setPort(8080);

        return serverFactory;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver(ServletContext context) {
        var resolver = new CommonsMultipartResolver(context);
        resolver.setMaxUploadSize(500000000);
        resolver.setMaxInMemorySize(500000000);
        return resolver;
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

}
