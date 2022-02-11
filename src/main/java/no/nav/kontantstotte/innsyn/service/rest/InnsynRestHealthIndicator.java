package no.nav.kontantstotte.innsyn.service.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Arrays;

class InnsynRestHealthIndicator implements HealthIndicator, EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(InnsynRestHealthIndicator.class);

    private final Counter pdlInnysynSuccess = Metrics.counter("pdl.innsyn.health", "response", "success");
    private final Counter pdlInnsynFailure = Metrics.counter("pdl.innsyn.health", "response", "failure");
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
            pdlInnysynSuccess.increment();
            LOG.info("PDL innsyn service is UP");
            return up();
        } catch (Exception e) {
            pdlInnsynFailure.increment();
            LOG.warn("Could not verify health of PDL innsyn service ", e);
            return isPreprod() || isProd() ? downWithDetails(e) : down();
        }
    }

    private static Health down() {
        return Health.down().build();
    }

    private Health downWithDetails(Exception e) {
        return Health.down().withDetail("PDL innsyn service", innsynServiceClient.toString()).withException(e).build();
    }

    private boolean isPreprod() {
        return Arrays.asList(env.getActiveProfiles()).contains("preprod");
    }

    private boolean isProd() {
        return Arrays.asList(env.getActiveProfiles()).contains("prod");
    }

    private boolean isDev() {
        return Arrays.asList(env.getActiveProfiles()).contains("dev");
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
    public void setEnvironment(@NotNull Environment env) {
        this.env = env;
    }
}
