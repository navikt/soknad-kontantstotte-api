package no.nav.kontantstotte.api.config;

import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.List;


@Provider
public class CORSResponseFilter implements ContainerResponseFilter {

    private List<String> allowedOriginsList;

    @Inject
    public CORSResponseFilter(@Value("${no.nav.kontantstotte.api.allowed.origins:" +
            "https://soknad-kontantstotte.nais.oera-q.local," +
            "https://soknad-kontantstotte-q.nav.no," +
            "https://soknad-kontantstotte.nav.no," +
            "}") String... allowedOrigins) {
        allowedOriginsList = Arrays.asList(allowedOrigins);
    }


    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {

        String origin = request.getHeaderString("Origin");
        if (allowedOriginsList.contains(origin)) {
            response.getHeaders().add("Access-Control-Allow-Origin", origin);
            response.getHeaders().add("Access-Control-Allow-Headers",
                    "origin, content-type, accept, authorization");
            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        }
    }
}
