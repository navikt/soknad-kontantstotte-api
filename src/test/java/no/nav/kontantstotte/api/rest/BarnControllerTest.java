package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.api.rest.dto.BarnDto;
import no.nav.kontantstotte.config.ApplicationConfig;
import no.nav.kontantstotte.innsyn.domain.Barn;
import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.security.token.support.core.JwtTokenConstants;
import no.nav.security.token.support.test.JwtTokenGenerator;
import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = {ApplicationConfig.class, TokenGeneratorConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class BarnControllerTest {

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
    public void at_uthenting_av_barninformasjon_er_korrekt() {
        Barn barn = barn1();
        when(innsynServiceMock.hentBarnInfo(any())).thenReturn(new ArrayList<Barn>() {{
            add(barn);
        }});

        Response response = kallEndepunkt();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        List<BarnDto> barnDtoList = response.readEntity(new GenericType<List<BarnDto>>() {
        });
        assertThat(barnDtoList.get(0).getFulltnavn()).isEqualTo(barn.getFulltnavn());
        assertThat(barnDtoList.get(0).getFødselsdato()).isEqualTo(barn.getFødselsdato());
        assertThat(barnDtoList.get(0).getFødselsnummer()).isEqualTo(barn.getFødselsnummer());
    }

    @Test
    public void at_uthenting_av_flerlinginformasjon_er_korrekt() {
        Barn tvilling1 = tvilling1();
        Barn tvilling2 = tvilling2();
        when(innsynServiceMock.hentBarnInfo(any())).thenReturn(new ArrayList<Barn>() {{
            add(tvilling1);
            add(tvilling2);
        }});

        Response response = kallEndepunkt();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        List<BarnDto> barnDtoList = response.readEntity(new GenericType<List<BarnDto>>() {
        });
        assertThat(barnDtoList.size()).isEqualTo(2);
        assertThat(barnDtoList.get(0).getFulltnavn()).isEqualTo(tvilling1.getFulltnavn());
        assertThat(barnDtoList.get(0).getFødselsdato()).isEqualTo(tvilling1.getFødselsdato());
        assertThat(barnDtoList.get(0).getFødselsnummer()).isEqualTo(tvilling1.getFødselsnummer());
        assertThat(barnDtoList.get(1).getFulltnavn()).isEqualTo(tvilling2.getFulltnavn());
        assertThat(barnDtoList.get(1).getFødselsdato()).isEqualTo(tvilling2.getFødselsdato());
        assertThat(barnDtoList.get(1).getFødselsnummer()).isEqualTo(tvilling2.getFødselsnummer());
    }

    @Test
    public void at_tps_feil_gir_500() {
        when(innsynServiceMock.hentBarnInfo(any())).thenThrow(new InnsynOppslagException("Feil i pdl"));
        Response response = kallEndepunkt();
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    public void at_tps_feil_legger_pa_cors_filter() {
        when(innsynServiceMock.hentBarnInfo(any())).thenThrow(new InnsynOppslagException("Feil i pdl"));
        Response response = kallEndepunkt();
        assertThat(response.getHeaders()).containsKey("Access-Control-Allow-Origin");
    }

    private Response kallEndepunkt() {
        String jwtToken = JwtTokenGenerator.createSignedJWT(INNLOGGET_BRUKER).serialize();
        return getClient().target("http://localhost:" + port + contextPath).path("/barn")
                          .request(MediaType.APPLICATION_JSON_TYPE)
                          .accept(MediaType.APPLICATION_JSON_TYPE)
                          .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer " + jwtToken)
                          .header("Referer", "https://soknad-kontantstotte-t.nav.no/")
                          .header("Origin", "https://soknad-kontantstotte-t.nav.no")
                          .get();
    }

    private Barn barn1() {
        return new Barn.Builder()
                .fulltnavn("NAVNESEN JENTEBARN")
                .fødselsnummer("11111111111")
                .fødselsdato("01.02.2018")
                .build();
    }

    private Barn tvilling1() {
        return new Barn.Builder()
                .fulltnavn("NAVNESEN TVILLING1")
                .fødselsnummer("11111111111")
                .fødselsdato("01.01.2018")
                .build();
    }

    private Barn tvilling2() {
        return new Barn.Builder()
                .fulltnavn("NAVNESEN TVILLING2 MELLOMNAVN")
                .fødselsnummer("2222222222")
                .fødselsdato("01.01.2018")
                .build();
    }

    private Client getClient() {
        return ClientBuilder.newClient().register(LoggingFeature.class);
    }
}
