package no.nav.kontantstotte;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.rules.ExternalResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.context.OIDCClaims;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.test.support.JwtTokenGenerator;

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
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        RequestContextHolder.currentRequestAttributes().setAttribute(OIDCConstants.OIDC_VALIDATION_CONTEXT, oidcValidationContext, RequestAttributes.SCOPE_REQUEST);
    }

    @Override
    protected void after() {
        RequestContextHolder.currentRequestAttributes().removeAttribute(OIDCConstants.OIDC_VALIDATION_CONTEXT, RequestAttributes.SCOPE_REQUEST);
    }
}
