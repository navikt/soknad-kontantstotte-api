package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.ArbeidIUtlandet;
import no.nav.kontantstotte.innsending.steg.Familieforhold;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.mockTekster;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.tekst;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ArbeidIUtlandetMappingTest {
    private final String sporsmal = "Mottar du ytelser fra utlandet?";
    private final String svar = "Ja";
    private final String tileggsSporsmal = "Oppgi land, utenlandsk id-nummer, adresse i landene og perioder";
    private final String tileggsSvar = "Søker oppgir land, utenlandsk id-nummer, adresse i landene og perioder hvor man mottok eller fortsatt mottar ytelser fra utlandet";
    private final String annenForelderSporsmal = "Mottar annen forelder ytelser fra utlandet?";
    private final String annenForelderSvar = "Nei";

    private Map<String, String> tekster = mockTekster(
            tekst(UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND, sporsmal),
            tekst(UTENLANDSKE_YTELSER_FORKLARING, tileggsSporsmal),
            tekst(UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND, annenForelderSporsmal),
            tekst(SVAR_JA, svar),
            tekst(SVAR_NEI, annenForelderSvar));

    private Unleash unleash;

    @Before
    public void init() {
        this.unleash = new FakeUnleash();
    }

    @Test
    public void arbeidIUtlandet_nar_foreldre_ikke_bor_sammen() {
        Soknad soknad = hentArbeidIUtlandetSoknad();

        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "NEI";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new ArbeidIUtlandetMapping(tekster).map(soknad, unleash);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, svar),
                        tuple(tileggsSporsmal, tileggsSvar)
                );
    }

    @Test
    public void arbeidIUtlandet_nar_foreldre_bor_sammen() {
        Soknad soknad = hentArbeidIUtlandetSoknad();

        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "JA";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new ArbeidIUtlandetMapping(tekster).map(soknad, unleash);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, svar),
                        tuple(tileggsSporsmal, tileggsSvar),
                        tuple(annenForelderSporsmal, annenForelderSvar)
                );
    }

    private Soknad hentArbeidIUtlandetSoknad() {
        Soknad soknad = new Soknad();
        ArbeidIUtlandet arbeidIUtlandet = new ArbeidIUtlandet();
        arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkel = "JA";
        arbeidIUtlandet.arbeiderAnnenForelderIUtlandetForklaring = tileggsSvar;
        arbeidIUtlandet.arbeiderAnnenForelderIUtlandet = "NEI";
        arbeidIUtlandet.arbeiderAnnenForelderIUtlandetForklaring = "";
        soknad.arbeidIUtlandet = arbeidIUtlandet;

        return soknad;
    }
}
