package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Familieforhold;
import no.nav.kontantstotte.innsending.steg.UtenlandskeYtelser;
import no.nav.kontantstotte.tekst.TekstService;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class UtenlandskeYtelserMappingTest {

    private static final Map<String, String> TEKSTER = new TekstService().hentTekster("nb");
    private static final String hentTekst(Tekstnokkel tekstnokkel) { return TEKSTER.get(tekstnokkel.getNokkel()); }

    private final String sporsmal = hentTekst(UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND);
    private final String svar = hentTekst(SVAR_JA);
    private final String tileggsSporsmal = hentTekst(UTENLANDSKE_YTELSER_FORKLARING);
    private final String tileggsSvar = "Søker oppgir land, utenlandsk id-nummer, adresse i landene og perioder hvor man mottok eller fortsatt mottar ytelser fra utlandet";
    private final String annenForelderSporsmal = hentTekst(UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND);
    private final String annenForelderSvar = hentTekst(SVAR_NEI);

    @Test
    public void utenlandskeYtelser_nar_foreldre_ikke_bor_sammen() {
        Soknad soknad = hentUtenlandskeYtelserSoknad();

        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "NEI";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new UtenlandskeYtelserMapping(new Tekster(TEKSTER)).map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, svar),
                        tuple(tileggsSporsmal, tileggsSvar)
                );
    }

    @Test
    public void utenlandskeYtelser_nar_foreldre_bor_sammen() {
        Soknad soknad = hentUtenlandskeYtelserSoknad();

        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "JA";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new UtenlandskeYtelserMapping(new Tekster(TEKSTER)).map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, svar),
                        tuple(tileggsSporsmal, tileggsSvar),
                        tuple(annenForelderSporsmal, annenForelderSvar)
                );
    }

    private Soknad hentUtenlandskeYtelserSoknad() {
        Soknad soknad = new Soknad();
        UtenlandskeYtelser utenlandskeYtelser = new UtenlandskeYtelser();
        utenlandskeYtelser.mottarYtelserFraUtland = "JA";
        utenlandskeYtelser.mottarYtelserFraUtlandForklaring = tileggsSvar;
        utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland = "NEI";
        utenlandskeYtelser.mottarAnnenForelderYtelserFraUtlandForklaring = "";
        soknad.utenlandskeYtelser = utenlandskeYtelser;

        return soknad;
    }
}
