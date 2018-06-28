package no.nav.kontantstotte.config;

import no.nav.kontantstotte.api.filter.CORSResponseFilter;
import no.nav.kontantstotte.api.rest.BarnResource;
import no.nav.kontantstotte.api.rest.StatusResource;
import no.nav.kontantstotte.api.rest.TeksterResource;
import no.nav.kontantstotte.api.rest.exceptionmapper.WebApplicationExceptionMapper;
import no.nav.security.oidc.jaxrs.OidcContainerRequestFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class RestConfiguration extends ResourceConfig {


    public RestConfiguration() {

        register(JacksonFeature.class);
        // Resources
        register(StatusResource.class);
        register(TeksterResource.class);
        register(BarnResource.class);
        // Filters
        register(CORSResponseFilter.class);
        register(OidcContainerRequestFilter.class);
        // ExceptionMappers
        register(WebApplicationExceptionMapper.class);
    }

}
