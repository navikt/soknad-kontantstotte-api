package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Familieforhold;
import no.nav.kontantstotte.innsending.steg.TilknytningTilUtland;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.mockTekster;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.tekst;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class TilknytningTilUtlandMappingTest {
    private final String sporsmal = "Har du bodd eller vært yrkesaktiv i Norge eller andre EØS-land i minst fem år tilsammen?";
    private final String svar = "Ja, i ett EØS-land";
    private final String tileggsSporsmal = "Oppgi land, utenlandsk ID-nummer, adresse i landene og perioder";
    private final String tileggsSvar = "Søker oppgir land, utenlandsk id-nummer, adresse i landene og perioder hvor man mottok eller fortsatt mottar ytelser fra utlandet";
    private final String annenForelderSporsmal = "Har den andre forelderen bodd eller vært yrkesaktiv i Norge eller andre EØS-land i minst fem år tilsammen?";
    private final String annenForelderSvar = "Nei, jeg har ikke bodd eller vært yrkesaktiv i Norge eller andre EØS-land i minst fem år tilsammen";

    private Map<String, String> tekster = mockTekster(
            tekst(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, sporsmal),
            tekst(TILKNYTNING_TIL_UTLAND_FORKLARING, tileggsSporsmal),
            tekst(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, annenForelderSporsmal),
            tekst(SVAR_JA_I_EOS, svar),
            tekst(TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE, annenForelderSvar));

    private FakeUnleash fakeUnleash;

    @Before
    public void setUp() {

        fakeUnleash = new FakeUnleash();
        UnleashProvider.initialize(fakeUnleash);
    }

    @After
    public void tearDown() {
        UnleashProvider.initialize(null);
    }

        @Test
    public void tilknytningTilUtland_nar_foreldre_ikke_bor_sammen() {
        Soknad soknad = hentTilknytningTilUtlandSoknad()
                .familieforhold(new Familieforhold(
                        false,
                        null,
                        null))
                .build();

        Bolk bolk = new TilknytningTilUtlandMapping(tekster).map(soknad);

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
        Soknad soknad = hentTilknytningTilUtlandSoknad()
                .familieforhold(new Familieforhold(
                        true,
                        null,
                        null))
                .build();

        Bolk bolk = new TilknytningTilUtlandMapping(tekster).map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, svar),
                        tuple(tileggsSporsmal, tileggsSvar),
                        tuple(annenForelderSporsmal, annenForelderSvar)
                );
    }

    private Soknad.Builder hentTilknytningTilUtlandSoknad() {
        return new Soknad.Builder()
                .tilknytningTilUtland(new TilknytningTilUtland(
                        "NEI",
                        "",
                        "JAIEOS",
                        tileggsSvar
                ));
    }
}
