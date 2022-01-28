package no.nav.kontantstotte.innsyn.service.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Arrays;

class InnsynRestHealthIndicator implements HealthIndicator, EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(InnsynRestHealthIndicator.class);

    private final Counter tpsInnysynSuccess = Metrics.counter("tps.innsyn.health", "response", "success");
    private final Counter tpsInnsynFailure = Metrics.counter("tps.innsyn.health", "response", "failure");
    private final InnsynService innsynServiceClient;

    private Environment env;

    InnsynRestHealthIndicator(InnsynService innsynServiceClient) {
        this.innsynServiceClient = innsynServiceClient;
    }

    @Override
    public Health health() {

        LOG.info("Pinging PDL innsyn service");
        try {
            innsynServiceClient.ping();
            tpsInnysynSuccess.increment();
            LOG.info("PDL innsyn service is UP");
            return up();
        } catch (Exception e) {
            tpsInnsynFailure.increment();
            LOG.warn("Could not verify health of PDL innsyn service ", e);
            return isPreprod() ? downWithDetails(e) : down();
        }
    }

    private static Health down() {
        return Health.down().build();
    }

    private Health downWithDetails(Exception e) {
        return Health.down().withDetail("PDL innsyn service", innsynServiceClient.toString()).withException(e).build();
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
        return Health.up().withDetail("PDL innsyn service", innsynServiceClient.toString()).build();
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
