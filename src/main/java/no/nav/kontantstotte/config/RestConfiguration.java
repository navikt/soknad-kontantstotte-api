package no.nav.kontantstotte.config;

import no.nav.kontantstotte.api.rest.StatusResource;
import no.nav.security.oidc.jaxrs.OidcContainerRequestFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class RestConfiguration extends ResourceConfig {


    public RestConfiguration() {

        register(JacksonFeature.class);
        register(StatusResource.class);
        register(OidcContainerRequestFilter.class);

    }

}
