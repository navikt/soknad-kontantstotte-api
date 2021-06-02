package no.nav.kontantstotte.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties(CorsProperties.class)
public class WebConfig implements WebMvcConfigurer {

    static Logger LOG = LoggerFactory.getLogger(WebConfig.class);
    CorsProperties corsProperties;

    WebConfig(CorsProperties corsProperties){
        this.corsProperties = corsProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        LOG.info("Allow origins: {}", corsProperties.getAllowedOrigins());
        registry.addMapping("/api/**").allowedOrigins(corsProperties.getAllowedOrigins());
    }
}
