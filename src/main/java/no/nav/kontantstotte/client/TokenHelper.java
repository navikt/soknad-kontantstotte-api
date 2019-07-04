package no.nav.kontantstotte.client;

import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.OIDCValidationContext;

public final class TokenHelper {
    private TokenHelper() {
    }

    public static String generatAuthorizationHeaderValueForLoggedInUser(OIDCRequestContextHolder contextHolder) {
        OIDCValidationContext context = contextHolder.getOIDCValidationContext();

        if (context != null && context.hasValidToken()) {
            StringBuilder headerValue = new StringBuilder();
            for (String issuer : context.getIssuers()) {
                headerValue.append("Bearer ").append(context.getToken(issuer).getIdToken());
            }
            return headerValue.toString();
        }

        return "";
    }
}
