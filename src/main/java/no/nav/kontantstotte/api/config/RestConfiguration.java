package no.nav.kontantstotte.api.config;

import no.nav.kontantstotte.api.rest.StatusResource;
import no.nav.kontantstotte.api.security.OidcJerseyRequestFilter;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.spring.oidc.SpringOIDCRequestContextHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;

public class RestConfiguration extends ResourceConfig {


    public RestConfiguration() {

        register(JacksonFeature.class);
        register(StatusResource.class);
        register(OidcJerseyRequestFilter.class);
        register(CORSResponseFilter.class);
    }

}
