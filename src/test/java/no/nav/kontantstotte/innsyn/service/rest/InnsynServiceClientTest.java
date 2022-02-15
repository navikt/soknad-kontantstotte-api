package no.nav.kontantstotte.innsyn.service.rest;

import no.nav.kontantstotte.innsyn.domain.Barn;
import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import no.nav.kontantstotte.innsyn.pdl.PdlApp2AppClient;
import no.nav.kontantstotte.innsyn.pdl.PdlClient;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlForelderBarnRelasjon;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlFødselsdato;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonBolk;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlNavn;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlPersonData;
import org.assertj.core.util.Lists;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InnsynServiceClientTest {

    public static final String TESTBARN1_IDENT = "testbarn1Ident";
    public static final String TESTBARN2_IDENT = "testbarn2Ident";
    public static final String TESTFNR = "testfnr";
    public static final String BARN1_FORNAVN = "barn1Fornavn";
    public static final String BARN1_ETTERNAVN = "barn1Etternavn";
    public static final String BARN2_FORNAVN = "barn2Fornavn";
    public static final String BARN2_ETTERNAVN = "barn2Etternavn";
    public static final String TESTFØDSELSDATO = LocalDate.now().minusMonths(15).toString();
    private final PdlClient mockPdlClient = mock(PdlClient.class);
    private final PdlApp2AppClient mockPdlSystemClient = mock(PdlApp2AppClient.class);
    private final InnsynServiceClient innsynServiceClient = new InnsynServiceClient(mockPdlClient, mockPdlSystemClient);

    @Test
    public void kan_ikke_hente_barninfo_når_det_ikke_finnes_barn() {
        when(mockPdlClient.hentPersoninfoMedRelasjoner(anyString())).thenReturn(Lists.emptyList());

        InnsynOppslagException exception = Assertions.assertThrows(InnsynOppslagException.class,
                                                                   () -> {
                                                                       innsynServiceClient.hentBarnInfo(TESTFNR);
                                                                   });
        assertEquals("Finnes ikke barn for søker", exception.getMessage());
    }

    @Test
    public void henter_barninfo_når_person_har_ett_barn() {
        when(mockPdlClient.hentPersoninfoMedRelasjoner(anyString()))
                .thenReturn(Lists.newArrayList(lagForeldreBarnRelasjon(TESTBARN1_IDENT)));
        when(mockPdlSystemClient.hentPersonerMedBolk(anyList())).thenReturn(Lists.newArrayList(new PdlHentPersonBolk(
                TESTBARN1_IDENT, lagPdlPersonData(BARN1_FORNAVN, BARN1_ETTERNAVN))));

        List<Barn> barnInfo = innsynServiceClient.hentBarnInfo(TESTFNR);
        assertEquals(1, barnInfo.size());
        Barn barn = barnInfo.get(0);
        assertEquals(String.join(" ", BARN1_FORNAVN, BARN1_ETTERNAVN), barn.getFulltnavn());
        assertEquals(TESTFØDSELSDATO, barn.getFødselsdato());
        assertEquals(TESTBARN1_IDENT, barn.getFødselsnummer());
    }

    @Test
    public void henter_barninfo_når_person_har_flere_barn() {
        when(mockPdlClient.hentPersoninfoMedRelasjoner(anyString()))
                .thenReturn(Lists.newArrayList(lagForeldreBarnRelasjon(TESTBARN1_IDENT),
                                               lagForeldreBarnRelasjon(TESTBARN2_IDENT)));
        when(mockPdlSystemClient.hentPersonerMedBolk(anyList()))
                .thenReturn(Lists.newArrayList(new PdlHentPersonBolk(TESTBARN1_IDENT,
                                                                     lagPdlPersonData(BARN1_FORNAVN, BARN1_ETTERNAVN)),
                                               new PdlHentPersonBolk(TESTBARN2_IDENT,
                                                                     lagPdlPersonData(BARN2_FORNAVN, BARN2_ETTERNAVN))));

        List<Barn> barnInfo = innsynServiceClient.hentBarnInfo(TESTFNR);
        assertEquals(2, barnInfo.size());
        Barn barn1 = barnInfo.get(0);
        assertEquals(String.join(" ", BARN1_FORNAVN, BARN1_ETTERNAVN), barn1.getFulltnavn());
        assertEquals(TESTFØDSELSDATO, barn1.getFødselsdato());
        assertEquals(TESTBARN1_IDENT, barn1.getFødselsnummer());

        Barn barn2 = barnInfo.get(1);
        assertEquals(String.join(" ", BARN2_FORNAVN, BARN2_ETTERNAVN), barn2.getFulltnavn());
        assertEquals(TESTFØDSELSDATO, barn2.getFødselsdato());
        assertEquals(TESTBARN2_IDENT, barn2.getFødselsnummer());
    }

    @NotNull
    private PdlForelderBarnRelasjon lagForeldreBarnRelasjon(String ident) {
        return new PdlForelderBarnRelasjon(
                ident,
                "BARN");
    }

    @NotNull
    private PdlPersonData lagPdlPersonData(String fornavn, String etternavn) {
        return new PdlPersonData(newArrayList(new PdlFødselsdato(TESTFØDSELSDATO)),
                                 newArrayList(new PdlNavn(fornavn,
                                                          null,
                                                          etternavn)),
                                 emptyList(),
                                 emptyList(),
                                 emptyList());
    }
}
