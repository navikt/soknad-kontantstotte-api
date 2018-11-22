package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Familieforhold;
import no.nav.kontantstotte.innsending.steg.UtenlandskeYtelser;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.mockTekster;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.tekst;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class UtenlandskeYtelserMappingTest {
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

    @Test
    public void utenlandskeYtelser_nar_foreldre_ikke_bor_sammen() {
        Soknad soknad = hentUtenlandskeYtelserSoknad()
                .familieforhold(new Familieforhold(
                        false,
                        null,
                        null))
                .build();

        Bolk bolk = new UtenlandskeYtelserMapping(tekster).map(soknad);

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
        Soknad soknad = hentUtenlandskeYtelserSoknad()
                .familieforhold(new Familieforhold(
                        true,
                        null,
                        null))
                .build();

        Bolk bolk = new UtenlandskeYtelserMapping(tekster).map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, svar),
                        tuple(tileggsSporsmal, tileggsSvar),
                        tuple(annenForelderSporsmal, annenForelderSvar)
                );
    }

    private Soknad.Builder hentUtenlandskeYtelserSoknad() {
        return new Soknad.Builder()
                .utenlandskeYtelser(new UtenlandskeYtelser(
                true,
                tileggsSvar,
                false,
                ""
        ));
    }
}
