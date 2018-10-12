package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.SokerKrav;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.mockTekster;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.tekst;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class KravTilSokerMappingTest {
    private Unleash unleash = new FakeUnleash();

    @Test
    public void kravTilSokerMapping() {
        String kravTilSokerTittel = "Kravene til elektronisk søknad";
        String kravTilSokerBarnIkkeHjemme = "Barnet er ikke adoptert, i fosterhjem eller på institusjon";
        String kravTilSokerBoddEllerJobberINorgeSisteFemAar = "Jeg har bodd eller vært yrkesaktiv i Norge eller et annet EØS-land i minst 5 år til sammen";
        String kravTilSokerBorSammenMedBarnet = "Jeg bor i Norge sammen med barnet";
        String kravTilSokerIkkeAvtaltDeltBosted = "Jeg og den andre forelderen har ikke avtalt delt bosted";
        String kravTilSokerNorskStatsborger = "Jeg er statsborger i Norge eller i et annet EØS-land";
        String kravTilSokerSkalBoMedBarnetINorgeNesteTolvMaaneder = "Jeg og barnet skal ikke oppholde oss i utlandet mer enn 3 måneder de neste 12 månedene";

        Map<String, String> tekster = mockTekster(
                tekst(KRAV_TIL_SOKER_TITTEL, kravTilSokerTittel),
                tekst(KRAV_TIL_SOKER_BARN_IKKE_HJEMME, kravTilSokerBarnIkkeHjemme),
                tekst(KRAV_TIL_SOKER_BODD_ELLER_JOBBET_I_NORGE_SISTE_FEM_AAR, kravTilSokerBoddEllerJobberINorgeSisteFemAar),
                tekst(KRAV_TIL_SOKER_BOR_SAMMEN_MED_BARNET, kravTilSokerBorSammenMedBarnet),
                tekst(KRAV_TIL_SOKER_IKKE_AVTALT_DELT_BOSTED, kravTilSokerIkkeAvtaltDeltBosted),
                tekst(KRAV_TIL_SOKER_NORSK_STATSBORGER, kravTilSokerNorskStatsborger),
                tekst(KRAV_TIL_SOKER_SKAL_BO_MED_BARNET_I_NORGE_NESTE_TOLV_MAANEDER, kravTilSokerSkalBoMedBarnetINorgeNesteTolvMaaneder));

        Soknad soknad = new Soknad();
        SokerKrav kravTilSoker = new SokerKrav();
        kravTilSoker.barnIkkeHjemme = "JA";
        kravTilSoker.boddEllerJobbetINorgeSisteFemAar = "JA";
        kravTilSoker.borSammenMedBarnet = "JA";
        kravTilSoker.ikkeAvtaltDeltBosted = "JA";
        kravTilSoker.norskStatsborger = "JA";
        kravTilSoker.skalBoMedBarnetINorgeNesteTolvMaaneder = "JA";
        soknad.kravTilSoker = kravTilSoker;

        Bolk kravTilSokerBolk = new KravTilSokerMapping(tekster).map(soknad, unleash);

        assertThat(kravTilSokerBolk)
                .extracting("tittel", "undertittel")
                .containsExactly(kravTilSokerTittel, null);

        List<Element> elementer = kravTilSokerBolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(null, kravTilSokerBarnIkkeHjemme),
                        tuple(null, kravTilSokerBoddEllerJobberINorgeSisteFemAar),
                        tuple(null, kravTilSokerBorSammenMedBarnet),
                        tuple(null, kravTilSokerIkkeAvtaltDeltBosted),
                        tuple(null, kravTilSokerNorskStatsborger),
                        tuple(null, kravTilSokerSkalBoMedBarnetINorgeNesteTolvMaaneder));
    }
}
