package no.nav.kontantstotte.api.security;

import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.context.OIDCClaims;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.spring.oidc.validation.api.Protected;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;
import no.nav.security.spring.oidc.validation.api.Unprotected;
import no.nav.security.spring.oidc.validation.interceptor.OIDCUnauthorizedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Provider
@ProtectedWithClaims(issuer = "")
public class OidcJerseyRequestFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(OidcJerseyRequestFilter.class);

    private final ResourceInfo resourceInfo;

    private final HttpServletRequest servletRequest;

    private final OIDCRequestContextHolder contextHolder;

    //private String[] ignoreConfig;
    //private Map<Object, Boolean> handlerFlags = new ConcurrentHashMap<>();

    @Inject
    public OidcJerseyRequestFilter(
            @Context ResourceInfo resourceInfo,
            @Context HttpServletRequest servletRequest,
            OIDCRequestContextHolder contextHolder) {
        this.contextHolder = contextHolder;
        this.servletRequest = servletRequest;

        /*if (enableOIDCTokenValidation != null) {
            ignoreConfig = enableOIDCTokenValidation.getStringArray("ignore");
            if (ignoreConfig == null) {
                ignoreConfig = new String[0];
            }
        }
        else {
            // nothing explicitly configured to be ignored, intercept everything
            ignoreConfig = new String[0];
        }*/
        this.resourceInfo = resourceInfo;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {

        logger.error("T E S T");

        OIDCValidationContext validationContext = (OIDCValidationContext) contextHolder
                .getRequestAttribute(OIDCConstants.OIDC_VALIDATION_CONTEXT);

        // Consider putting it on threadlocal
        // OIDCValidationContext validationContext = (OIDCValidationContext) servletRequest.getAttribute(OIDCConstants.OIDC_VALIDATION_CONTEXT);

            /*HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (shouldIgnore(handlerMethod.getBean())) {
                return true;
            }*/
        Unprotected unprotectedAnnotation = getMethodAnnotation(Unprotected.class);
        if (unprotectedAnnotation != null) {
            logger.debug("method " + resourceInfo.getResourceMethod() + " marked @Unprotected");
            return;
        }
        ProtectedWithClaims withClaimsAnnotation = getMethodAnnotation(ProtectedWithClaims.class);
        if (withClaimsAnnotation != null) {
            logger.debug("method " + resourceInfo.getResourceMethod() + " marked @ProtectedWithClaims");
            handleProtectedWithClaimsAnnotation(validationContext, withClaimsAnnotation);
            return;
        } else {
            Protected protectedAnnotation = getMethodAnnotation(Protected.class);
            if (protectedAnnotation != null) {
                logger.debug("method " + resourceInfo.getResourceMethod() + " marked @Protected");
                handleProtectedAnnotation(validationContext);
                return;
            }
        }

        Class<?> declaringClass = resourceInfo.getResourceClass();
        if (declaringClass.isAnnotationPresent(Unprotected.class)) {
            logger.debug("method " + resourceInfo.getResourceMethod() + " marked @Unprotected throug annotation on class");
            return;
        }

        if (declaringClass.isAnnotationPresent(ProtectedWithClaims.class)) {
            logger.debug("method " + resourceInfo.getResourceMethod() + " marked @ProtectedWithClaims");
            handleProtectedWithClaimsAnnotation(validationContext,
                    declaringClass.getAnnotation(ProtectedWithClaims.class));
            return;
        } else {
            if (declaringClass.isAnnotationPresent(Protected.class)) {
                logger.debug("method " + resourceInfo.getResourceMethod() + " marked @Protected");
                handleProtectedAnnotation(validationContext);
                return;
            }
        }
        logger.debug("method " + resourceInfo.getResourceMethod() + " not marked, access denied (returning NOT_IMPLEMENTED)");

        // TODO throw proper exception
        throw new RuntimeException("FAIL!");
/*
            throw new OIDCUnauthorizedException("Server misconfigured - controller/method ["
                    + handlerMethod.getBean().getClass().getName() + "." + handlerMethod.getMethod().getName()
                    + "] not annotated @Unprotected, @Protected or added to ignore list");
*/


    }
    private <T extends Annotation> T getMethodAnnotation(Class<T> annotation) {

        return resourceInfo.getResourceMethod().getDeclaredAnnotation(annotation);
    }

    private boolean handleProtectedAnnotation(OIDCValidationContext validationContext) {
        if (validationContext.hasValidToken()) {
            return true;
        }
        logger.error("no token found in validation context");
        throw new OIDCUnauthorizedException("Authorization token required");
    }

    private void handleProtectedWithClaimsAnnotation(OIDCValidationContext validationContext,
                                                          ProtectedWithClaims annotation) {
        String issuer = annotation.issuer();
        String[] claims = annotation.claimMap();
        if (StringUtils.isNotBlank(issuer)) {
            OIDCClaims tokenClaims = validationContext.getClaims(issuer);
            if (tokenClaims == null) {
                logger.error(String.format(
                        "could not find token for issuer '%s' in validation context. check your configuration.",
                        issuer));
                throw new OIDCUnauthorizedException("Authorization token not authorized");
            }
            if (!containsRequiredClaims(tokenClaims, claims)) {
                logger.error("token does not contain all annotated claims");
                throw new OIDCUnauthorizedException("Authorization token not authorized");
            }
        }
    }

    private boolean containsRequiredClaims(OIDCClaims tokenClaims, String... claims) {
        for (String string : claims) {
            String name = StringUtils.substringBefore(string, "=").trim();
            String value = StringUtils.substringAfter(string, "=").trim();
            if (StringUtils.isNotBlank(name)) {
                if (!tokenClaims.containsClaim(name, value)) {
                    logger.debug(String.format("token does not contain %s = %s", name, value));
                    return false;
                }
            }
        }
        return true;
    }
/*
    private boolean shouldIgnore(Object object) {
        Boolean flag = handlerFlags.get(object);
        if (flag != null) {
            return flag;
        }
        String fullName = object.getClass().getName();
        for (String ignore : ignoreConfig) {
            if (fullName.startsWith(ignore)) {
                logger.info("Adding " + fullName + " to OIDC validation ignore list");
                handlerFlags.put(object, true);
                return true;
            }
        }
        logger.info("Adding " + fullName + " to OIDC validation interceptor list");
        handlerFlags.put(object, false);
        return false;
    }

*/

}
