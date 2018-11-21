package no.nav.kontantstotte.innsending.oppsummering.html.mapping;


import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.UtenlandskKontantstotte;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.SoknadTilOppsummeringTest.NEI;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.mockTekster;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.tekst;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class UtenlandskKontantstotteMappingTest {

    private static final String JA = "Ja";
    private static final String TITTEL = "Utenlandsk kontantstotte";
    private static final String SPORSMAL = "Får du kontantstøtte fra andre land enn Norge??";
    private static final String FRITEKSTSPORSMAL = "Oppgi landene, beløpet du får utbetalt og perioden for utbetalingen";

    private Soknad soknad;
    private UtenlandskKontantstotte utenlandskKontantstotte;
    private Map<String, String> tekster;
    private UtenlandskKontantstotteMapping mapping;

    @Before
    public void setUp() {
        soknad = new Soknad.Builder()
                .utenlandskKontantstotte(new UtenlandskKontantstotte(null, null))
                .build();

        tekster = mockTekster(
                tekst(UTENLANDSK_KONTANTSTOTTE_TITTEL, TITTEL),
                tekst(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE, SPORSMAL),
                tekst(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE_TILLEGGSINFO, FRITEKSTSPORSMAL),
                tekst(SVAR_NEI, NEI), tekst(SVAR_JA, JA));

        mapping = new UtenlandskKontantstotteMapping(tekster);
    }

    @Test
    public void skal_ha_rett_tittel_og_undertitte() {
        Bolk bolk = mapping.map(utenlandskKontantstotteSoknad(null, null));
        assertThat(bolk)
                .extracting("tittel", "undertittel")
                .containsExactly(TITTEL, null);
    }

    @Test
    public void mottar_ikke_utenlandsk_kontantstotte() {
        Bolk bolk = mapping.map(utenlandskKontantstotteSoknad("NEI", null));

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(SPORSMAL, NEI));
    }

    @Test
    public void mottar_utenlandsk_kontantstotte() {
        String fritekstsvar = "Utfyllende info fra soker";

        Bolk bolk = mapping.map(utenlandskKontantstotteSoknad("JA", fritekstsvar));

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(SPORSMAL, JA),
                        tuple(FRITEKSTSPORSMAL, fritekstsvar));
    }

    private Soknad utenlandskKontantstotteSoknad(String mottarKontantstotteFraUtlandet, String fritekstsvar) {
        return new Soknad.Builder()
                .utenlandskKontantstotte(new UtenlandskKontantstotte(mottarKontantstotteFraUtlandet, fritekstsvar))
                .build();
    }

}