package no.nav.kontantstotte.config;

import no.nav.kontantstotte.api.filter.CORSResponseFilter;
import no.nav.kontantstotte.api.rest.*;
import no.nav.kontantstotte.api.rest.exceptionmapper.PersonOppslagExceptionMapper;
import no.nav.kontantstotte.api.rest.exceptionmapper.SkjermetAdresseExceptionMapper;
import no.nav.kontantstotte.api.rest.exceptionmapper.WebApplicationExceptionMapper;
import no.nav.kontantstotte.person.domain.SkjermetAdresseException;
import no.nav.security.oidc.jaxrs.OidcContainerRequestFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class RestConfiguration extends ResourceConfig {


    public RestConfiguration() {

        register(JacksonFeature.class);
        register(MultiPartFeature.class);
        // Resources
        register(TeksterResource.class);
        register(PersonResource.class);
        register(SokerResource.class);
        register(InnsendingResource.class);
        register(InnloggingStatusResource.class);
        // ExceptionMappers
        register(WebApplicationExceptionMapper.class);
        register(SkjermetAdresseExceptionMapper.class);
        register(PersonOppslagExceptionMapper.class);
        // Filters
        register(CORSResponseFilter.class);
        register(OidcContainerRequestFilter.class);
    }

}
