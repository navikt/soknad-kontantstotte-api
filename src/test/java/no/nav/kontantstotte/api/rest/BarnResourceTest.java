package no.nav.kontantstotte.api.rest;

import com.nimbusds.jwt.SignedJWT;
import no.nav.kontantstotte.api.rest.dto.BarnDto;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.innsyn.domain.Barn;
import no.nav.kontantstotte.innsyn.domain.IInnsynServiceClient;
import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.test.support.JwtTokenGenerator;
import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ApplicationConfig.class, TokenGeneratorConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class BarnResourceTest {

    public static final String INNLOGGET_BRUKER = "12345678911";
    @Value("${local.server.port}")
    private int port;

    @Value("${spring.jersey.application-path}")
    private String contextPath;

    @Inject
    private IInnsynServiceClient innsynServiceMock;

    @After
    public void tearDown() {
        reset(innsynServiceMock);
    }

    @Test
    public void at_uthenting_av_barninformasjon_er_korrekt() {
        Barn barn = barn1();
        when(innsynServiceMock.hentBarnInfo(any())).thenReturn(new ArrayList<Barn>() {{
            add(barn);
        }});

        Response response = kallEndepunkt();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        List<BarnDto> barnDtoList = response.readEntity(new GenericType<List<BarnDto>>() {});
        assertThat(barnDtoList.get(0).getFulltnavn()).isEqualTo(barn.getFulltnavn());
        assertThat(barnDtoList.get(0).getFodselsdato()).isEqualTo(barn.getFodselsdato());
        assertThat(barnDtoList.get(0).getErFlerling()).isEqualTo(false);
    }

    @Test
    public void at_uthenting_av_flerlinginformasjon_er_korrekt() {
        Barn flerling1 = flerling1();
        Barn flerling2 = flerling2();
        when(innsynServiceMock.hentBarnInfo(any())).thenReturn(new ArrayList<Barn>() {{
            add(flerling1);
            add(flerling2);
        }});

        Response response = kallEndepunkt();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        List<BarnDto> barnDtoList = response.readEntity(new GenericType<List<BarnDto>>() {});

        assertThat(barnDtoList.get(0).getFulltnavn()).isEqualTo(flerling1.getFulltnavn());
        assertThat(barnDtoList.get(0).getFodselsdato()).isEqualTo(flerling1.getFodselsdato());
        assertThat(barnDtoList.get(0).getErFlerling()).isEqualTo(false);

        assertThat(barnDtoList.get(1).getFulltnavn()).isEqualTo(flerling2.getFulltnavn());
        assertThat(barnDtoList.get(1).getFodselsdato()).isEqualTo(flerling2.getFodselsdato());
        assertThat(barnDtoList.get(1).getErFlerling()).isEqualTo(false);

        assertThat(barnDtoList.get(2).getFulltnavn()).isEqualTo("GUTTEBARN1 og GUTTEBARN2");
        assertThat(barnDtoList.get(2).getFodselsdato()).isEqualTo(flerling1.getFodselsdato() + " og " + flerling2.getFodselsdato());
        assertThat(barnDtoList.get(2).getErFlerling()).isEqualTo(true);
    }

    @Test
    public void at_tps_feil_gir_500() {
        when(innsynServiceMock.hentBarnInfo(any())).thenThrow(new InnsynOppslagException("Feil i tps"));
        Response response = kallEndepunkt();
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    public void at_tps_feil_legger_pa_cors_filter() {
        when(innsynServiceMock.hentBarnInfo(any())).thenThrow(new InnsynOppslagException("Feil i tps"));
        Response response = kallEndepunkt();
        assertThat(response.getHeaders()).containsKey("Access-Control-Allow-Origin");
    }

    private Response kallEndepunkt() {
        WebTarget target = ClientBuilder.newClient().register(LoggingFeature.class).target("http://localhost:" + port + contextPath);
        SignedJWT signedJWT = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER);
        return target.path("/barn")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header(OIDCConstants.AUTHORIZATION_HEADER, "Bearer " + signedJWT.serialize())
                .header("Referer", "https://soknad-kontantstotte-t.nav.no/")
                .header("Origin", "https://soknad-kontantstotte-t.nav.no")
                .get();
    }

    private Barn barn1() {
        return new Barn.Builder()
                .fulltnavn("NAVNESEN JENTEBARN")
                .fodselsdato("01.01.2017")
                .build();
    }

    private Barn flerling1() {
        return new Barn.Builder()
                .fulltnavn("NAVNESEN GUTTEBARN1")
                .fodselsdato("01.01.2018")
                .build();
    }

    private Barn flerling2() {
        return new Barn.Builder()
                .fulltnavn("NAVNESEN GUTTEBARN2")
                .fodselsdato("04.01.2018")
                .build();
    }
}

