package no.nav.kontantstotte.api.rest;

import no.nav.security.oidc.api.Unprotected;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.ResourceBundle.getBundle;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("tekster")
@Unprotected
public class TeksterResource {

    private static final String BUNDLE_NAME = "tekster";

    private static final String[] VALID_LANGUAGES = { "nb", "nn" };

    private final Map<String, Map<String, String>> properties;

    public TeksterResource() {

        Function<ResourceBundle, Map<String, String>> bundleToMap = bundle -> bundle.keySet().stream()
                .collect(toMap(identity(), bundle::getString));

        properties = Arrays.stream(VALID_LANGUAGES)
                .map(language -> getBundle(BUNDLE_NAME, new Locale(language)))
                .collect(toMap(bundle -> bundle.getLocale().getLanguage(), bundleToMap));
    }

    @GET
    @Path("{language}")
    public Map<String, String> tekster(@PathParam("language") String language) {
        return properties.get(language);
    }
}
