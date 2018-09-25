package no.nav.kontantstotte.config.toggle;
import no.finn.unleash.DefaultUnleash;
import no.finn.unleash.Unleash;
import no.finn.unleash.strategy.Strategy;
import no.finn.unleash.util.UnleashConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile({"!dev"})
public class FeatureToggleConfig {
    private static final String APP_NAME_PROPERTY_NAME = "${APP_NAME}";
    private static final String UNLEASH_API_URL_PROPERTY_NAME = "${UNLEASH_API_URL}";
    private static final String FASIT_ENVIRONMENT_NAME = "${FASIT_ENVIRONMENT_NAME}";

    @Bean
    public Unleash unleash(
            @Value(APP_NAME_PROPERTY_NAME) String appName,
            @Value(UNLEASH_API_URL_PROPERTY_NAME) String unleashApiUrl,
            Strategy... strategies
    ) {
        UnleashConfig config = UnleashConfig.builder()
                .appName(appName)
                .unleashAPI(unleashApiUrl)
                .build();

        return new DefaultUnleash(config, strategies);
    }

    @Bean
    public Strategy isNotProd(@Value(FASIT_ENVIRONMENT_NAME) String env){
        return new IsNotProdStrategy(env);
    }

    @Bean
    public Strategy byEnvironment(@Value(FASIT_ENVIRONMENT_NAME) String env){
        return new ByEnvironmentStrategy(env);
    }
}