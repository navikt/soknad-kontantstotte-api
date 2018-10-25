package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Familieforhold;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.tekst;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.mockTekster;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class FamilieforholdMappingTest {

    private static final String JA = "Ja";
    private static final String NEI = "Nei";

    @Test
    public void familieforhold_nar_foreldre_ikke_bor_sammen() {
        String tittel = "Familieforhold";
        String sporsmal = "Bor du sammen med den andre forelderen?";

        Map<String, String> tekster = mockTekster(
                tekst(FAMILIEFORHOLD_TITTEL, tittel),
                tekst(FAMILIEFORHOLD_BOR_SAMMEN, sporsmal),
                tekst(SVAR_NEI, NEI));

        Soknad soknad = new Soknad();
        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "NEI";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new FamilieforholdMapping(tekster).map(soknad);
        assertThat(bolk)
                .extracting("tittel", "undertittel")
                .containsExactly(tittel, null);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, NEI));
    }

    @Test
    public void familieforhold_nar_foreldre_bor_sammen() {
        String sporsmal = "Bor du sammen med den andre forelderen?";
        String sporsmal_navn = "Navnet til den andre forelderen:";
        String sporsmal_fnr = "Fødselsnummeret til den andre forelderen:";

        Map<String, String> tekster = mockTekster(
                tekst(FAMILIEFORHOLD_BOR_SAMMEN, sporsmal),
                tekst(FAMILIEFORHOLD_NAVN_ANNEN_FORELDER, sporsmal_navn),
                tekst(FAMILIEFORHOLD_FNR_ANNEN_FORELDER, sporsmal_fnr),
                tekst(SVAR_JA, JA));

        Soknad soknad = new Soknad();
        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "JA";
        familieforhold.annenForelderNavn = "NN";
        familieforhold.annenForelderFodselsnummer = "XXXXXX";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new FamilieforholdMapping(tekster).map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, JA),
                        tuple(sporsmal_navn, familieforhold.annenForelderNavn),
                        tuple(sporsmal_fnr, familieforhold.annenForelderFodselsnummer)
                );
    }

}