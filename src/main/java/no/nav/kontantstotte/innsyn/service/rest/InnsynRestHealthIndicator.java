package no.nav.kontantstotte.innsyn.service.rest;

import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.metrics.MetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Arrays;

class InnsynRestHealthIndicator implements HealthIndicator, EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(InnsynRestHealthIndicator.class);

    private final InnsynService innsynServiceClient;
    private final MetricService metricService;

    private Environment env;

    InnsynRestHealthIndicator(InnsynService innsynServiceClient, MetricService metricService) {
        this.innsynServiceClient = innsynServiceClient;
        this.metricService = metricService;
    }

    @Override
    public Health health() {

        LOG.info("Pinging TPS innsyn service");
        try {
            innsynServiceClient.ping();
            metricService.getTpsInnsynHealth().inc();
            return up();
        } catch (Exception e) {
            metricService.getTpsInnsynHealth().dec();
            LOG.warn("Could not verify health of TPS innsyn service ", e);
            return isPreprod() ? downWithDetails(e) : up();
        }
    }

    private static Health down() {
        return Health.down().build();
    }

    private Health downWithDetails(Exception e) {
        return Health.down().withDetail("TPS innsyn service", innsynServiceClient.toString()).withException(e).build();
    }

    private boolean isPreprod() {
        return env.acceptsProfiles("preprod");
    }

    private boolean isDev() {
        return env.acceptsProfiles("dev");
    }

    private static Health up() {
        return Health.up().build();
    }

    private Health upWithDetails() {
        return Health.up().withDetail("TPS innsyn service", innsynServiceClient.toString()).build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pinger=" + innsynServiceClient.toString() + ", activeProfiles "
                + Arrays.toString(env.getActiveProfiles()) + "]";
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }
}
