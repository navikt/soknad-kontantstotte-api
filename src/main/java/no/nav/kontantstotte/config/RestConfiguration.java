package no.nav.kontantstotte.config;

import no.nav.kontantstotte.api.filter.CORSResponseFilter;
import no.nav.kontantstotte.api.rest.*;
import no.nav.kontantstotte.api.rest.exceptionmapper.InnsendingExceptionMapper;
import no.nav.kontantstotte.api.rest.exceptionmapper.InnsynOppslagExceptionMapper;
import no.nav.kontantstotte.api.rest.exceptionmapper.FortroligAdresseExceptionMapper;
import no.nav.kontantstotte.api.rest.exceptionmapper.StorageExceptionMapper;
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
        register(SokerResource.class);
        register(BarnResource.class);
        register(InnsendingResource.class);
        register(InnloggingStatusResource.class);
        register(StorageResource.class);
        // ExceptionMappers
        register(FortroligAdresseExceptionMapper.class);
        register(InnsynOppslagExceptionMapper.class);
        register(InnsendingExceptionMapper.class);
        register(StorageExceptionMapper.class);
        // Filters
        register(CORSResponseFilter.class);
        register(OidcContainerRequestFilter.class);
    }

}
