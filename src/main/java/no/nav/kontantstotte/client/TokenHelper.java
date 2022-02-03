package no.nav.kontantstotte.client;

import no.nav.security.token.support.core.context.TokenValidationContext;
import no.nav.security.token.support.core.context.TokenValidationContextHolder;

public final class TokenHelper {

    private TokenHelper() {
    }

    public static String generateAuthorizationHeaderValueForLoggedInUser(TokenValidationContextHolder contextHolder) {
        TokenValidationContext context = contextHolder.getTokenValidationContext();

        if (context != null && context.hasValidToken()) {
            StringBuilder headerValue = new StringBuilder();
            for (String issuer : context.getIssuers()) {
                headerValue.append("Bearer ").append(context.getJwtToken(issuer));
            }
            return headerValue.toString();
        }

        return "";
    }
}
