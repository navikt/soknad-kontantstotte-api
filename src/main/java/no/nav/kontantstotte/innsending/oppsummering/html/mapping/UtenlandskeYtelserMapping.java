package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.steg.UtenlandskeYtelser;

import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class UtenlandskeYtelserMapping extends BolkMapping {
    public UtenlandskeYtelserMapping(Map<String, String> tekster) {
        super(tekster);
    }


    @Override
    public Bolk map(Soknad soknad) {

        UtenlandskeYtelser utenlandskeYtelser = soknad.getUtenlandskeYtelser();

        Bolk utenlandskeYtelserBolk = new Bolk()
                .medTittel(tekst(UTENLANDSKE_YTELSER_TITTEL));

        if (utenlandskeYtelser.mottarYtelserFraUtland) {
            utenlandskeYtelserBolk.add(svar(UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND, SVAR_JA));
            utenlandskeYtelserBolk.add(svar(UTENLANDSKE_YTELSER_FORKLARING, utenlandskeYtelser.mottarYtelserFraUtlandForklaring));
        } else {
            utenlandskeYtelserBolk.add(svar(UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND, SVAR_NEI));
        }

        if (soknad.getFamilieforhold().borForeldreneSammenMedBarnet) {
            if (utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland) {
                utenlandskeYtelserBolk.add(svar(UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND, SVAR_JA));
                utenlandskeYtelserBolk.add(svar(UTENLANDSKE_YTELSER_FORKLARING, utenlandskeYtelser.mottarAnnenForelderYtelserFraUtlandForklaring));
            } else {
                utenlandskeYtelserBolk.add(svar(UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND, SVAR_NEI));
            }
        }

        return utenlandskeYtelserBolk;
    }
}
