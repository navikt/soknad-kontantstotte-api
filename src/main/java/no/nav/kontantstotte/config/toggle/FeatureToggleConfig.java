package no.nav.kontantstotte.config.toggle;

import no.finn.unleash.DefaultUnleash;
import no.finn.unleash.Unleash;
import no.finn.unleash.strategy.Strategy;
import no.finn.unleash.util.UnleashConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;


@Configuration
public class FeatureToggleConfig {
    private static final String APP_NAME_PROPERTY_NAME = "${NAIS_APP_NAME}";
    private static final String UNLEASH_API_URL_PROPERTY_NAME = "${UNLEASH_API_URL}";
    private static final String ENVIRONMENT_NAME = "${ENVIRONMENT_NAME}";

    @Autowired
    private Unleash unleash;

    @Profile("!dev")
    @Bean
    @Scope(SCOPE_SINGLETON)
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

    @Profile("!dev")
    @Bean
    public Strategy isNotProd(@Value(ENVIRONMENT_NAME) String env){
        return new IsNotProdStrategy(env);
    }

    @Profile("!dev")
    @Bean
    public Strategy byEnvironment(@Value(ENVIRONMENT_NAME) String env){
        return new ByEnvironmentStrategy(env);
    }

    @PostConstruct
    public void initUnleashProvider() {
        UnleashProvider.initialize(unleash);
    }
}
