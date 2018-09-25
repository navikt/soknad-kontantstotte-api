package no.nav.kontantstotte.config.toggle;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class ByEnvironmentStrategyTest {
    private ByEnvironmentStrategy byEnvironmentStrategy;

    @Before
    public void init() {
        byEnvironmentStrategy = new ByEnvironmentStrategy("t6");
    }

    @Test
    public void testIsEnabled() {
        Map<String, String> parameters = ImmutableMap.of("miljø", "t6, q0, p");

        assertThat(byEnvironmentStrategy.isEnabled(parameters)).isTrue();
    }

    @Test
    public void testEmptyParameters() {
        Map<String, String> emptyParameters = ImmutableMap.of();

        assertThat(byEnvironmentStrategy.isEnabled(emptyParameters)).isFalse();
    }

    @Test
    public void testNotInEnvironment() {
        Map<String, String> parameters = ImmutableMap.of("miljø", "q0, p");

        assertThat(byEnvironmentStrategy.isEnabled(parameters)).isFalse();
    }
}