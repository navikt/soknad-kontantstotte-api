package no.nav.kontantstotte.api.config;

import no.nav.kontantstotte.api.rest.StatusResource;
import no.nav.kontantstotte.api.sec.OidcJerseyRequestFilter;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.spring.oidc.SpringOIDCRequestContextHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;

public class RestConfiguration extends ResourceConfig {


    public RestConfiguration() {

        packages("no.nav.kontantstotte");
        register(JacksonFeature.class);

        register(new AbstractBinder() {

            @Override
            protected void configure() {
                bind(SpringOIDCRequestContextHolder.class).to(OIDCRequestContextHolder.class).to(Singleton.class);
            }

        });
    }

}
