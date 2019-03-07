package no.nav.kontantstotte.metrics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfiguration {

    @Bean
    public MetricService metricService() {
        return new MetricService();
    }
}
