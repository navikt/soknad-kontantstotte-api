package no.nav.kontantstotte.api;

import no.nav.security.spring.oidc.test.TokenGeneratorConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TokenGeneratorConfiguration.class)
public class TestConfig {


}
