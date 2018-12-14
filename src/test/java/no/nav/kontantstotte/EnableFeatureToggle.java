package no.nav.kontantstotte;

import no.finn.unleash.FakeUnleash;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import org.junit.rules.ExternalResource;

public class EnableFeatureToggle extends ExternalResource {

    private final String toggleName;

    public EnableFeatureToggle(String toggleName) {
        this.toggleName = toggleName;
    }

    @Override
    protected void before() {
        FakeUnleash fakeUnleash = new FakeUnleash();
        fakeUnleash.enable(toggleName);
        UnleashProvider.initialize(fakeUnleash);
    }

    @Override
    protected void after() {
        UnleashProvider.initialize(null);
    }
}
