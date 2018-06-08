package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.api.Launcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Launcher.class)
public class StatusResourceTest {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void testStatus() throws Exception {
        WebTarget target = ClientBuilder.newClient().target("http://localhost:" + port);
        Response response = target.path("/status/ping").request().get();

        assertThat(response.getStatus(), is(equalTo(200)));
        String value = response.readEntity(String.class);
    }

}
