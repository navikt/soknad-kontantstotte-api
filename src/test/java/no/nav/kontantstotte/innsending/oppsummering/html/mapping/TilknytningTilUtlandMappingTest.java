package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Familieforhold;
import no.nav.kontantstotte.innsending.steg.TilknytningTilUtland;
import no.nav.kontantstotte.tekst.TekstService;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class TilknytningTilUtlandMappingTest {

    private static final Map<String, String> TEKSTER = new TekstService().hentTekster("nb");
    private static final String hentTekst(Tekstnokkel tekstnokkel) { return TEKSTER.get(tekstnokkel.getNokkel()); }

    private final String sporsmal = hentTekst(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR);
    private final String svar = hentTekst(SVAR_JA_I_EOS);
    private final String tileggsSporsmal = hentTekst(TILKNYTNING_TIL_UTLAND_FORKLARING);
    private final String tileggsSvar = "Søker oppgir land, utenlandsk id-nummer, adresse i landene og perioder hvor man mottok eller fortsatt mottar ytelser fra utlandet";
    private final String annenForelderSporsmal =  hentTekst(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER);
    private final String annenForelderSvar = hentTekst(TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE);

        @Test
    public void tilknytningTilUtland_nar_foreldre_ikke_bor_sammen() {
        Soknad soknad = hentTilknytningTilUtlandSoknad();

        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "NEI";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new TilknytningTilUtlandMapping(new Tekster(TEKSTER)).map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, svar),
                        tuple(tileggsSporsmal, tileggsSvar)
                );
    }

    @Test
    public void tilknytningTilUtland_nar_foreldre_bor_sammen() {
        Soknad soknad = hentTilknytningTilUtlandSoknad();

        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "JA";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new TilknytningTilUtlandMapping(new Tekster(TEKSTER)).map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, svar),
                        tuple(tileggsSporsmal, tileggsSvar),
                        tuple(annenForelderSporsmal, annenForelderSvar)
                );
    }

    private Soknad hentTilknytningTilUtlandSoknad() {
        Soknad soknad = new Soknad();
        TilknytningTilUtland tilknytningTilUtland = new TilknytningTilUtland();
        tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar = "JAIEOS";
        tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAarForklaring = tileggsSvar;
        tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar = "NEI";
        tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAarForklaring = "";
        soknad.tilknytningTilUtland = tilknytningTilUtland;

        return soknad;
    }
}
