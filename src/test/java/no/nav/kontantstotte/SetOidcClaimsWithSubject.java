package no.nav.kontantstotte;

import no.finn.unleash.FakeUnleash;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.security.oidc.context.OIDCClaims;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.jaxrs.OidcRequestContext;
import no.nav.security.oidc.test.support.JwtTokenGenerator;
import org.junit.rules.ExternalResource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SetOidcClaimsWithSubject extends ExternalResource {

    private final String fnr;

    public SetOidcClaimsWithSubject(String fnr) {
        this.fnr = fnr;
    }

    @Override
    protected void before() {
        OIDCClaims oidcClaims = new OIDCClaims(JwtTokenGenerator.createSignedJWT(fnr));

        OIDCValidationContext oidcValidationContext = mock(OIDCValidationContext.class);
        when(oidcValidationContext.getClaims(any())).thenReturn(oidcClaims);
        OidcRequestContext.getHolder().setOIDCValidationContext(oidcValidationContext);
    }

    @Override
    protected void after() {
        OidcRequestContext.getHolder().setOIDCValidationContext(null);
    }
}
