package no.nav.kontantstotte.person.service.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.person.domain.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Arrays;

class PersonRestHealthIndicator implements HealthIndicator, EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(PersonRestHealthIndicator.class);

    private final Counter tpsPersonSuccess = Metrics.counter("tps.person.health", "response", "success");
    private final Counter tpsPersonFailure = Metrics.counter("tps.person.health", "response", "failure");
    private final PersonService personService;

    private Environment env;

    PersonRestHealthIndicator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public Health health() {

        LOG.info("Pinging TPS person service");
        try {
            personService.ping();
            tpsPersonSuccess.increment();
            return up();
        } catch (Exception e) {
            tpsPersonFailure.increment();
            LOG.warn("Could not verify health of TPS person service ", e);
            return isPreprod() ? downWithDetails(e) : up();
        }
    }

    private static Health down() {
        return Health.down().build();
    }

    private Health downWithDetails(Exception e) {
        return Health.down().withDetail("TPS person service", personService.toString()).withException(e).build();
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
        return Health.up().withDetail("TPS person service", personService.toString()).build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pinger=" + personService.toString() + ", activeProfiles "
                + Arrays.toString(env.getActiveProfiles()) + "]";
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }
}
