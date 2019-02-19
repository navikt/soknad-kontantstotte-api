package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.ArbeidIUtlandet;
import no.nav.kontantstotte.innsending.steg.Familieforhold;
import no.nav.kontantstotte.tekst.DefaultTekstProvider;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ArbeidIUtlandetMappingTest {

    private static final Map<String, String> TEKSTER = new DefaultTekstProvider().hentTekster("nb");
    private static final String hentTekst(Tekstnokkel tekstnokkel) { return TEKSTER.get(tekstnokkel.getNokkel()); }

    private final String sporsmal = hentTekst(ARBEID_I_UTLANDET_ELLER_KONTINENTALSOKKEL);
    private final String svar = hentTekst(SVAR_JA);
    private final String tileggsSporsmal = hentTekst(ARBEID_I_UTLANDET_FORKLARING);
    private final String tileggsSvar = "Søker oppgir land, utenlandsk id-nummer, adresse i landene og perioder hvor man mottok eller fortsatt mottar ytelser fra utlandet";
    private final String annenForelderSporsmal =  hentTekst(ARBEID_I_UTLANDET_ARBEIDER_ANNEN_FORELDER_I_UTLANDET);
    private final String annenForelderSvar = hentTekst(SVAR_NEI);

    @Test
    public void arbeidIUtlandet_nar_foreldre_ikke_bor_sammen() {
        Soknad soknad = hentArbeidIUtlandetSoknad();

        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "NEI";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new ArbeidIUtlandetMapping(new Tekster(TEKSTER)).map(soknad);

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

        Bolk bolk = new ArbeidIUtlandetMapping(new Tekster(TEKSTER)).map(soknad);

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
        arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkelForklaring = tileggsSvar;
        arbeidIUtlandet.arbeiderAnnenForelderIUtlandet = "NEI";
        arbeidIUtlandet.arbeiderAnnenForelderIUtlandetForklaring = "";
        soknad.arbeidIUtlandet = arbeidIUtlandet;

        return soknad;
    }
}
