package no.nav.kontantstotte.innsyn.pdl;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlError;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlForelderBarnRelasjon;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlFødselsdato;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonResponse;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlPerson;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlPersonData;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.List;

import static org.assertj.core.util.Lists.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PdlClientTest {

    private final RestOperations restTemplateMock = Mockito.mock(RestOperations.class);
    private final PdlClient pdlClient = new PdlClient(URI.create("testurl"), restTemplateMock, new ObjectMapper());

    @Test
    public void hentPersoninfoMedRelasjoner_med_suksess_respons() {
        when(restTemplateMock.exchange(anyString(),
                                       any(HttpMethod.class),
                                       any(HttpEntity.class),
                                       ArgumentMatchers.<Class<PdlHentPersonResponse>>any()))
                .thenReturn(ResponseEntity.ok(lagRespons(false)));

        List<PdlForelderBarnRelasjon> relasjoner = pdlClient.hentPersoninfoMedRelasjoner("testident");
        assertNotNull(relasjoner);
        assertEquals(1, relasjoner.size());
        PdlForelderBarnRelasjon relasjon = relasjoner.get(0);
        assertEquals("barnident", relasjon.getRelatertPersonsIdent());
        assertEquals("BARN", relasjon.getRelatertPersonsRolle());
    }

    @Test
    public void hentPersoninfoMedRelasjoner_med_feil_respons_fra_PDL() {
        when(restTemplateMock.exchange(anyString(),
                                       any(HttpMethod.class),
                                       any(HttpEntity.class),
                                       ArgumentMatchers.<Class<PdlHentPersonResponse>>any()))
                .thenReturn(ResponseEntity.ok(lagRespons(true)));

        InnsynOppslagException exception = assertThrows(InnsynOppslagException.class, () -> {
            pdlClient.hentPersoninfoMedRelasjoner("testident");
        });
        assertEquals("Feil ved oppslag på person:Feil respons", exception.getMessage());
    }

    @Test
    public void hentPersoninfoMedRelasjoner_med_feil_respons_når_PDL_har_nedetid() {
        when(restTemplateMock.exchange(anyString(),
                                       any(HttpMethod.class),
                                       any(HttpEntity.class),
                                       ArgumentMatchers.<Class<PdlHentPersonResponse>>any()))
                .thenThrow(new RestClientException("Kan ikke oppnå PDL"));

        InnsynOppslagException exception = assertThrows(InnsynOppslagException.class, () -> {
            pdlClient.hentPersoninfoMedRelasjoner("testident");
        });
        assertEquals("Kan ikke oppnå PDL", exception.getMessage());
    }

    private PdlHentPersonResponse lagRespons(boolean feilRespons) {
        PdlPersonData personData = new PdlPersonData(List.of(new PdlFødselsdato("testdato")),
                                                     emptyList(),
                                                     emptyList(),
                                                     emptyList(),
                                                     List.of(new PdlForelderBarnRelasjon("barnident",
                                                                                         "BARN")));
        return new PdlHentPersonResponse(new PdlPerson(personData),
                                         feilRespons ? List.of(new PdlError("Feil respons", emptyList())) : emptyList());
    }
}
