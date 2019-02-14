package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Barn;
import no.nav.kontantstotte.tekst.DefaultTekstProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class BarnMappingTest {

    private static final Map<String, String> TEKSTER = new DefaultTekstProvider().hentTekster("nb");
    private static final String hentTekst(Tekstnokkel tekstnokkel) { return TEKSTER.get(tekstnokkel.getNokkel()); }

    private static final String TITTEL = hentTekst(BARN_TITTEL);
    private static final String UNDERTITTEL = hentTekst(BARN_UNDERTITTEL);
    private static final String NAVN = hentTekst(BARN_NAVN);
    private static final String FODSELSDATO = hentTekst(BARN_FODSELSDATO);
    private static final String ADVARSEL = hentTekst(BARN_ADVARSEL);

    private Soknad soknad;
    private Barn innsendtBarn;
    private BarnMapping barnMapping;

    @Before
    public void setUp() {
        innsendtBarn = new Barn();
        soknad = new Soknad();
        soknad.mineBarn = innsendtBarn;
        barnMapping = new BarnMapping(new Tekster(TEKSTER));
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
                        tuple(FODSELSDATO, null, ADVARSEL));

        innsendtBarn.erFlerling = "NEI";
        elementer = barnMapping.map(soknad).elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar", "advarsel")
                .contains(
                        tuple(FODSELSDATO, null, null));
    }
}
