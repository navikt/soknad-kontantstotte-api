package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.config.ApplicationConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ApplicationConfig.class})
public class TeksterControllerTest {

    @Value("${local.server.port}")
    private int port;

    private String contextPath = "/api";
    @Test
    public void at_tekster_hentes_korrekt() {
        Response response = kallEndepunkt();
        Map<String, Map<String, String>> tekster = response.readEntity(new GenericType<Map<String, Map<String,String>>>() {});
        assertThat(tekster.size()).isEqualTo(2);
        assertThat(tekster.get("nn").get("barn.navn")).isEqualTo("Namn");
        assertThat(tekster.get("nb").get("barn.navn")).isEqualTo("Navn");
    }

    private Response kallEndepunkt() {
        WebTarget target = ClientBuilder.newClient().register(LoggingFeature.class).target("http://localhost:" + port + contextPath);
        return target.path("/tekster")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
