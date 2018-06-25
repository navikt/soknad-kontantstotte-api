package no.nav.kontantstotte.api;

import no.nav.kontantstotte.config.RestConfiguration;
import no.nav.security.oidc.test.support.jersey.TestTokenGeneratorResource;

public class TestRestConfiguration extends RestConfiguration {

    public TestRestConfiguration() {

        register(TestTokenGeneratorResource.class);

    }

}
