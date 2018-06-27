package no.nav.kontantstotte.api.rest;

import no.nav.security.oidc.api.Unprotected;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("tekster")
@Unprotected
public class TeksterResource {

    private static final String TEKSTER_FILENAME = "tekster.properties";
    private final Properties properties;

    public TeksterResource() {
        properties = new Properties();
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(TEKSTER_FILENAME);
        try {
            properties.load(new InputStreamReader(resourceAsStream, Charset.forName("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Feil ved lesing av tekster. Finnes filen %s i resources?", TEKSTER_FILENAME));
        }
    }

    @GET
    public Properties tekster() {
        return properties;
    }
}
