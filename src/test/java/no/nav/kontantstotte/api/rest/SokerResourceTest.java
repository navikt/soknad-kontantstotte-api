package no.nav.kontantstotte.api.rest;

import com.nimbusds.jwt.SignedJWT;
import no.nav.kontantstotte.api.rest.dto.SokerDto;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import no.nav.kontantstotte.innsyn.domain.FortroligAdresseException;
import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.test.support.JwtTokenGenerator;
import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ApplicationConfig.class, TokenGeneratorConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class SokerResourceTest {

    public static final String INNLOGGET_BRUKER = "12345678911";
    @Value("${local.server.port}")
    private int port;

    private String contextPath = "/api";

    @MockBean
    private InnsynService innsynServiceMock;

    @After
    public void tearDown() {
        reset(innsynServiceMock);
    }

    @Test
    public void at_uthenting_av_sokerinformasjon_er_korrekt() {
        when(innsynServiceMock.hentPersonInfo(any())).thenReturn(new Person.Builder().build());

        Response response = kallEndepunkt();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        SokerDto soker = response.readEntity(SokerDto.class);
        assertThat(soker.getInnloggetSom()).isEqualTo(INNLOGGET_BRUKER);
    }

    @Test
    public void at_tps_feil_gir_500() {
        when(innsynServiceMock.hentPersonInfo(any())).thenThrow(new InnsynOppslagException("Feil i tps"));
        Response response = kallEndepunkt();
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    public void at_skjermet_adresse_gir_403() {
        when(innsynServiceMock.hentPersonInfo(any())).thenThrow(new FortroligAdresseException("Skjermet adresse"));
        Response response = kallEndepunkt();
        assertThat(response.getStatus()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void at_tps_feil_legger_pa_cors_filter() {
        when(innsynServiceMock.hentPersonInfo(any())).thenThrow(new InnsynOppslagException("Feil i tps"));
        Response response = kallEndepunkt();
        assertThat(response.getHeaders()).containsKey("Access-Control-Allow-Origin");
    }

    @Test
    public void at_skjermet_adresse_feil_legger_pa_cors_filter() {
        when(innsynServiceMock.hentPersonInfo(any())).thenThrow(new FortroligAdresseException("Skjermet adresse"));
        Response response = kallEndepunkt();
        assertThat(response.getHeaders()).containsKey("Access-Control-Allow-Origin");
    }

    private Response kallEndepunkt() {

        WebTarget target = ClientBuilder.newClient().register(LoggingFeature.class).target("http://localhost:" + port + contextPath);
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);
        return target.path("/soker")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header(OIDCConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                .header("Referer", "https://soknad-kontantstotte-t.nav.no/")
                .header("Origin", "https://soknad-kontantstotte-t.nav.no")
                .get();
    }
}

