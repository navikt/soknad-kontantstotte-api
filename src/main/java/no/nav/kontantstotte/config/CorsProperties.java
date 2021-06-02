package no.nav.kontantstotte.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Arrays;
import java.util.Set;

@ConfigurationProperties(prefix = "cors")
@ConstructorBinding
public class CorsProperties {

    String[] allowedOrigins;

    public CorsProperties(Set<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins.toArray(String[]::new);
    }

    public String[] getAllowedOrigins() {
        return allowedOrigins;
    }
}
