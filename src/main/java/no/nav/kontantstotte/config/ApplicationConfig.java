package no.nav.kontantstotte.config;

import no.nav.familie.http.config.RestTemplateAzure;
import no.nav.familie.http.interceptor.BearerTokenClientCredentialsClientInterceptor;
import no.nav.familie.http.interceptor.BearerTokenExchangeClientInterceptor;
import no.nav.familie.http.interceptor.ConsumerIdClientInterceptor;
import no.nav.familie.http.interceptor.MdcValuesPropagatingClientInterceptor;
import no.nav.familie.log.filter.LogFilter;
import no.nav.kontantstotte.api.filter.SecurityHttpHeaderFilter;
import no.nav.kontantstotte.config.toggle.FeatureToggleConfig;
import no.nav.kontantstotte.innsending.InnsendingConfiguration;
import no.nav.kontantstotte.innsyn.service.rest.InnsynRestConfiguration;
import no.nav.security.token.support.client.spring.oauth2.DefaultOAuth2HttpClient;
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestOperations;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@SpringBootConfiguration
@ComponentScan({"no.nav.kontantstotte"})
@Import({FeatureToggleConfig.class, InnsendingConfiguration.class, InnsynRestConfiguration.class,
         MdcValuesPropagatingClientInterceptor.class,
         ConsumerIdClientInterceptor.class,
         BearerTokenExchangeClientInterceptor.class,
         BearerTokenClientCredentialsClientInterceptor.class,
         RestTemplateAzure.class})
@EnableOAuth2Client(cacheEnabled = true)
@EnableJwtTokenValidation(ignore = {"org.springframework", "org.springdoc"})
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

    @Bean("clientCredential")
    public RestOperations restTemplate(BearerTokenClientCredentialsClientInterceptor bearerTokenClientCredentialsClientInterceptor,
                                       MdcValuesPropagatingClientInterceptor mdcValuesPropagatingClientInterceptor,
                                       ConsumerIdClientInterceptor consumerIdClientInterceptor) {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.of(5, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(25, ChronoUnit.SECONDS))
                .interceptors(bearerTokenClientCredentialsClientInterceptor,
                              mdcValuesPropagatingClientInterceptor,
                              consumerIdClientInterceptor)
                .build();
    }

    @Bean("tokenExchange")
    public RestOperations restTemplate(BearerTokenExchangeClientInterceptor bearerTokenExchangeClientInterceptor,
                                       MdcValuesPropagatingClientInterceptor mdcValuesPropagatingClientInterceptor,
                                       ConsumerIdClientInterceptor consumerIdClientInterceptor) {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.of(5, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(25, ChronoUnit.SECONDS))
                .interceptors(bearerTokenExchangeClientInterceptor,
                              mdcValuesPropagatingClientInterceptor,
                              consumerIdClientInterceptor).build();
    }

    @Primary
    @Bean
    public DefaultOAuth2HttpClient oAuth2HttpClient() {
        return new DefaultOAuth2HttpClient(new RestTemplateBuilder().setConnectTimeout(Duration.of(5, ChronoUnit.SECONDS))
                                                                    .setReadTimeout(Duration.of(25, ChronoUnit.SECONDS)));
    }

}
