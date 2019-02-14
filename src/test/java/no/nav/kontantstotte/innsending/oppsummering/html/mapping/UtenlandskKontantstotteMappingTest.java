package no.nav.kontantstotte.innsending.oppsummering.html.mapping;


import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.UtenlandskKontantstotte;
import no.nav.kontantstotte.tekst.DefaultTekstProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class UtenlandskKontantstotteMappingTest {

    private static final Map<String, String> TEKSTER = new DefaultTekstProvider().hentTekster("nb");
    private static final String hentTekst(Tekstnokkel tekstnokkel) { return TEKSTER.get(tekstnokkel.getNokkel()); }

    private static final String JA = hentTekst(SVAR_JA);
    private static final String TITTEL = hentTekst(UTENLANDSK_KONTANTSTOTTE_TITTEL);
    private static final String SPORSMAL = hentTekst(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE);
    private static final String FRITEKSTSPORSMAL = hentTekst(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE_TILLEGGSINFO);
    private static final String NEI = hentTekst(SVAR_NEI);

    private Soknad soknad;
    private UtenlandskKontantstotte utenlandskKontantstotte;
    private UtenlandskKontantstotteMapping mapping;

    @Before
    public void setUp() {
        soknad = new Soknad();
        utenlandskKontantstotte = new UtenlandskKontantstotte();
        soknad.utenlandskKontantstotte = utenlandskKontantstotte;

        mapping = new UtenlandskKontantstotteMapping(new Tekster(TEKSTER));
    }

    @Test
    public void skal_ha_rett_tittel_og_undertitteø() {
        Bolk bolk = mapping.map(soknad);
        assertThat(bolk)
                .extracting("tittel", "undertittel")
                .containsExactly(TITTEL, null);
    }

    @Test
    public void mottar_ikke_utenlandsk_kontantstotte() {
        utenlandskKontantstotte.mottarKontantstotteFraUtlandet = "NEI";

        Bolk bolk = mapping.map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(SPORSMAL, NEI));
    }

    @Test
    public void mottar_utenlandsk_kontantstotte() {
        String fritekstsvar = "Utfyllende info fra soker";

        utenlandskKontantstotte.mottarKontantstotteFraUtlandet = "JA";
        utenlandskKontantstotte.mottarKontantstotteFraUtlandetTilleggsinfo = fritekstsvar;
        Bolk bolk = mapping.map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(SPORSMAL, JA),
                        tuple(FRITEKSTSPORSMAL, fritekstsvar));
    }

}
