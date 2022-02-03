package no.nav.kontantstotte.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private final String bearer = "bearer";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("Søknad kontantstøtte api"))
                            .components(new Components().addSecuritySchemes(bearer, getBearerTokenSecurityScheme()))
                            .addSecurityItem(new SecurityRequirement().addList(bearer, List.of("read", "write")));
    }

    private SecurityScheme getBearerTokenSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .scheme(bearer)
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }
}
