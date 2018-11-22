package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.steg.UtenlandskeYtelser;

import java.util.ArrayList;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class UtenlandskeYtelserMapping extends BolkMapping {
    public UtenlandskeYtelserMapping(Map<String, String> tekster) {
        super(tekster);
    }


    @Override
    public Bolk map(Soknad soknad) {
        Bolk utenlandskeYtelserBolk = new Bolk();

        UtenlandskeYtelser utenlandskeYtelser = soknad.getUtenlandskeYtelser();
        utenlandskeYtelserBolk.tittel = tekster.get(UTENLANDSKE_YTELSER_TITTEL.getNokkel());
        utenlandskeYtelserBolk.elementer = new ArrayList<>();

        if (utenlandskeYtelser.mottarYtelserFraUtland) {
            utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND, SVAR_JA));
            utenlandskeYtelserBolk.elementer.add(nyttElementMedVerdisvar.apply(UTENLANDSKE_YTELSER_FORKLARING, utenlandskeYtelser.mottarYtelserFraUtlandForklaring));
        } else {
            utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND, SVAR_NEI));
        }

        if (soknad.getFamilieforhold().borForeldreneSammenMedBarnet) {
            if (utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland) {
                utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND, SVAR_JA));
                utenlandskeYtelserBolk.elementer.add(nyttElementMedVerdisvar.apply(UTENLANDSKE_YTELSER_FORKLARING, utenlandskeYtelser.mottarAnnenForelderYtelserFraUtlandForklaring));
            } else {
                utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND, SVAR_NEI));
            }
        }

        return utenlandskeYtelserBolk;
    }
}
