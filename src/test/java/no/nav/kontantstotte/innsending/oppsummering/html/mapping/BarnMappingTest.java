package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Barn;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.mockTekster;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.tekst;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class BarnMappingTest {


    private static final String TITTEL = "BARN";
    private static final String UNDERTITTEL = "Barn du søker kontantstøtte for";
    private static final String NAVN = "Navn";
    private static final String FODSELSDATO = "Fødselsdato";
    private static final String ERFLERLING = "Er Flerling";
    private static final String ERFLERLING_ADVARSEL = "Vi ser at du søker for mer enn et barn. Vi forutsetter at opplysningene du oppgir om barna er like, om ikke må du søke for hvert av barna eller søke på papir.";
    private static final String JA = "JA";
    private static final String NEI = "NEI";

    private static final  Map<String, String> TEKSTER = mockTekster(
            tekst(BARN_TITTEL, TITTEL),
            tekst(BARN_UNDERTITTEL, UNDERTITTEL),
            tekst(BARN_NAVN, NAVN),
            tekst(BARN_FODSELSDATO, FODSELSDATO),
            tekst(BARN_ERFLERLING, ERFLERLING),
            tekst(BARN_ERFLERLING_ADVARSEL, ERFLERLING_ADVARSEL),
            tekst(SVAR_NEI, NEI),
            tekst(SVAR_JA, JA));

    private Soknad soknad;
    private Barn innsendtBarn;
    private BarnMapping barnMapping;

    @Before
    public void setUp() {
        innsendtBarn = new Barn();
        soknad = new Soknad();
        soknad.mineBarn = innsendtBarn;
        barnMapping = new BarnMapping(TEKSTER);
    }

    @Test
    public void tilBarneBolk() {
        innsendtBarn.navn = "TEST TESTESEN";
        innsendtBarn.fodselsdato = "01.01.2019";

        Bolk bolk = barnMapping.map(soknad);
        assertThat(bolk)
                .extracting("tittel", "undertittel")
                .containsExactly(TITTEL, UNDERTITTEL);

        List<Element> elementer = bolk.elementer;

        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(NAVN, innsendtBarn.navn),
                        tuple(FODSELSDATO, innsendtBarn.fodselsdato));
    }

    @Test
    public void at_flerlinger_gir_advarsel() {
        List<Element> elementer;

        innsendtBarn.erFlerling = "JA";
        elementer = barnMapping.map(soknad).elementer;

        assertThat(elementer)
                .extracting("sporsmal", "svar", "advarsel")
                .contains(
                        tuple(ERFLERLING, JA, ERFLERLING_ADVARSEL));

        innsendtBarn.erFlerling = "NEI";
        elementer = barnMapping.map(soknad).elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar", "advarsel")
                .contains(
                        tuple(ERFLERLING, NEI, null));
    }
}
