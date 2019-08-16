package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Familieforhold;
import no.nav.kontantstotte.tekst.TekstService;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class FamilieforholdMappingTest {

    private static final Map<String, String> TEKSTER = new TekstService().hentTekster("nb");
    private static final String hentTekst(Tekstnokkel tekstnokkel) { return TEKSTER.get(tekstnokkel.getNokkel()); }

    private static final String JA = hentTekst(SVAR_JA);
    private static final String NEI = hentTekst(SVAR_NEI);
    private static final String TITTEL = hentTekst(FAMILIEFORHOLD_TITTEL);
    private static final String SPORSMAL = hentTekst(FAMILIEFORHOLD_BOR_SAMMEN);
    private static final String SPORSMAL_NAVN = hentTekst(FAMILIEFORHOLD_NAVN_ANNEN_FORELDER);
    private static final String SPORSMAL_FNR = hentTekst(FAMILIEFORHOLD_FNR_ANNEN_FORELDER);


    @Test
    public void familieforhold_nar_foreldre_ikke_bor_sammen() {

        Soknad soknad = new Soknad();
        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "NEI";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new FamilieforholdMapping(new Tekster(TEKSTER)).map(soknad);
        assertThat(bolk)
                .extracting("tittel", "undertittel")
                .containsExactly(TITTEL, null);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(SPORSMAL, NEI));
    }

    @Test
    public void familieforhold_nar_foreldre_bor_sammen() {

        Soknad soknad = new Soknad();
        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "JA";
        familieforhold.annenForelderNavn = "NN";
        familieforhold.annenForelderFødselsnummer = "XXXXXX";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new FamilieforholdMapping(new Tekster(TEKSTER)).map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(SPORSMAL, JA),
                        tuple(SPORSMAL_NAVN, familieforhold.annenForelderNavn),
                        tuple(SPORSMAL_FNR, familieforhold.annenForelderFødselsnummer)
                );
    }

}
