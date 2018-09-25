package no.nav.kontantstotte.config.toggle;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;


public class ByEnvironmentStrategyTest {
    private ByEnvironmentStrategy byEnvironmentStrategy;

    @Before
    public void init() {
        byEnvironmentStrategy = new ByEnvironmentStrategy("t6");
    }

    @Test
    public void testIsEnabled() {
        Map<String, String> parameters = ImmutableMap.of("miljø", "t6, q0, p");
        Map<String, String> emptyParameters = ImmutableMap.of();

        assertTrue(byEnvironmentStrategy.isEnabled(parameters));
        assertFalse(byEnvironmentStrategy.isEnabled(emptyParameters));
    }
}