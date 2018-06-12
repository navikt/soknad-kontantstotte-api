package no.nav.kontantstotte.api.security;

import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.OIDCValidationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

import static java.util.Collections.singletonList;

@Component
public class OidcJerseyClientFilter implements ClientRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(OidcJerseyClientFilter.class);

    private final OIDCRequestContextHolder contextHolder;

    @Inject
    public OidcJerseyClientFilter(OIDCRequestContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        OIDCValidationContext context = (OIDCValidationContext) contextHolder
                .getRequestAttribute(OIDCConstants.OIDC_VALIDATION_CONTEXT);

        if(context != null) {
            logger.debug("adding tokens to Authorization header");
            StringBuilder headerValue = new StringBuilder();
            for(String issuer : context.getIssuers()) {
                logger.debug("adding token for issuer {}", issuer);
                headerValue.append("Bearer ").append(context.getToken(issuer).getIdToken());
            }
            requestContext.getHeaders().put(OIDCConstants.AUTHORIZATION_HEADER, singletonList(headerValue.toString()));
        } else {
            logger.debug("no tokens found, nothing added to request");
        }
    }

}
