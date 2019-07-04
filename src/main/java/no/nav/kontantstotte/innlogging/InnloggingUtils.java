package no.nav.kontantstotte.innlogging;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.context.OIDCValidationContext;

public class InnloggingUtils {

    private static final String SELVBETJENING = "selvbetjening";

    public static String hentFnrFraToken() {
        OIDCValidationContext context = (OIDCValidationContext) RequestContextHolder.currentRequestAttributes().getAttribute(OIDCConstants.OIDC_VALIDATION_CONTEXT, RequestAttributes.SCOPE_REQUEST);
        context = context != null ? context : new OIDCValidationContext();
        return context.getClaims(SELVBETJENING).getClaimSet().getSubject();
    }

}
