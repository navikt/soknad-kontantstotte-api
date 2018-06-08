package no.nav.kontantstotte.api.rest;

import com.nimbusds.jwt.SignedJWT;
import no.nav.kontantstotte.api.Launcher;
import no.nav.security.oidc.OIDCConstants;
import no.nav.security.spring.oidc.test.JwtTokenGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Launcher.class)
public class StatusResourceTest {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void testStatus() throws Exception {

        WebTarget target = ClientBuilder.newClient().target("http://localhost:" + port);
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT("12345678911");
        Response response = target.path("/status/ping")
                .request()
                .header(OIDCConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                .get();

        assertThat(response.getStatus(), is(equalTo(200)));
        String value = response.readEntity(String.class);
    }

}
