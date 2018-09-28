package no.nav.kontantstotte.config;

import no.nav.kontantstotte.api.filter.CORSResponseFilter;
import no.nav.kontantstotte.api.rest.*;
import no.nav.kontantstotte.api.rest.exceptionmapper.WebApplicationExceptionMapper;
import no.nav.security.oidc.jaxrs.OidcContainerRequestFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class RestConfiguration extends ResourceConfig {


    public RestConfiguration() {

        register(JacksonFeature.class);
        register(MultiPartFeature.class);
        // Resources
        register(InternalResource.class);
        register(TeksterResource.class);
        register(PersonResource.class);
        register(SokerResource.class);
        register(InnsendingResource.class);
        // Filters
        register(CORSResponseFilter.class);
        register(OidcContainerRequestFilter.class);
        // ExceptionMappers
        register(WebApplicationExceptionMapper.class);
    }

}
