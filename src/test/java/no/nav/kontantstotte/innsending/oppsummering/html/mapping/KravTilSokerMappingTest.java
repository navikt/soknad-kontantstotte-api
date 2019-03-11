package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.SokerKrav;
import no.nav.kontantstotte.tekst.TekstService;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class KravTilSokerMappingTest {

    private static final Map<String, String> TEKSTER = new TekstService().hentTekster("nb");
    private static final String hentTekst(Tekstnokkel tekstnokkel) { return TEKSTER.get(tekstnokkel.getNokkel()); }

    @Test
    public void kravTilSokerMapping() {
        String kravTilSokerTittel = hentTekst(KRAV_TIL_SOKER_TITTEL);
        String kravTilSokerBarnIkkeHjemme = hentTekst(KRAV_TIL_SOKER_BARN_IKKE_HJEMME);
        String kravTilSokerBorSammenMedBarnet = hentTekst(KRAV_TIL_SOKER_BOR_SAMMEN_MED_BARNET);
        String kravTilSokerIkkeAvtaltDeltBosted = hentTekst(KRAV_TIL_SOKER_IKKE_AVTALT_DELT_BOSTED);
        String kravTilSokerSkalBoMedBarnetINorgeNesteTolvMaaneder = hentTekst(KRAV_TIL_SOKER_SKAL_BO_MED_BARNET_I_NORGE_NESTE_TOLV_MAANEDER);

        Soknad soknad = new Soknad();
        SokerKrav kravTilSoker = new SokerKrav();
        kravTilSoker.barnIkkeHjemme = "JA";
        kravTilSoker.borSammenMedBarnet = "JA";
        kravTilSoker.ikkeAvtaltDeltBosted = "JA";
        kravTilSoker.skalBoMedBarnetINorgeNesteTolvMaaneder = "JA";
        soknad.kravTilSoker = kravTilSoker;

        Bolk kravTilSokerBolk = new KravTilSokerMapping(new Tekster(TEKSTER)).map(soknad);

        assertThat(kravTilSokerBolk)
                .extracting("tittel", "undertittel")
                .containsExactly(kravTilSokerTittel, null);

        List<Element> elementer = kravTilSokerBolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(null, kravTilSokerBarnIkkeHjemme),
                        tuple(null, kravTilSokerBorSammenMedBarnet),
                        tuple(null, kravTilSokerIkkeAvtaltDeltBosted),
                        tuple(null, kravTilSokerSkalBoMedBarnetINorgeNesteTolvMaaneder));
    }
}
