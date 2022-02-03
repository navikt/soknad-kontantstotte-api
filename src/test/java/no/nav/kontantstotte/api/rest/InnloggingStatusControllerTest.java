package no.nav.kontantstotte.api.rest;

import com.nimbusds.jwt.SignedJWT;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.security.token.support.core.JwtTokenConstants;
import no.nav.security.token.support.test.JwtTokenGenerator;
import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration;
import org.glassfish.jersey.logging.LoggingFeature;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ApplicationConfig.class,
                                                                                       TokenGeneratorConfiguration.class})
public class InnloggingStatusControllerTest {

    @Value("${local.server.port}")
    private int port;

    private String contextPath = "/api";

    @Test
    @Deprecated
    public void pingSkalGi200MedGyldigToken() {

        WebTarget target = client().target("http://localhost:" + port + contextPath);
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT("12345678911");
        Response response = target.path("/status/ping")
                .request()
                .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                .get();

        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }

    @Test
    @Deprecated
    public void pingSkalGi401UtenToken() {
        WebTarget target = client().target("http://localhost:" + port + contextPath);
        Response response = target.path("/status/ping")
                                  .request()
                                  .get();

        assertThat(response.getStatus(), is(equalTo(Response.Status.UNAUTHORIZED.getStatusCode())));
    }

    @Test
    public void skalGi200MedGyldigToken() {

        WebTarget target = client().target("http://localhost:" + port + contextPath);
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT("12345678911");
        Response response = target.path("/verify/loggedin")
                                  .request()
                                  .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                                  .get();

        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }

    @Test
    public void skalGi401UtenToken() {
        WebTarget target = client().target("http://localhost:" + port + contextPath);
        Response response = target.path("/verify/loggedin")
                                  .request()
                                  .get();

        assertThat(response.getStatus(), is(equalTo(Response.Status.UNAUTHORIZED.getStatusCode())));
    }

    private Client client() {
        return ClientBuilder.newClient().register(LoggingFeature.class);
    }

}
