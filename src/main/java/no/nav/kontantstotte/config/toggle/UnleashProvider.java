package no.nav.kontantstotte.config.toggle;

import no.finn.unleash.Unleash;

/**
 * Provides an Unleash toggle implementation from a static context. That way we don't need to modify injected
 * dependencies to toggle features.
 */
public class UnleashProvider {

    private static Unleash unleash;

    public static void initialize(Unleash unleash) {
        UnleashProvider.unleash = unleash;
    }

    public static Unleash get() {
        return unleash;
    }
}
