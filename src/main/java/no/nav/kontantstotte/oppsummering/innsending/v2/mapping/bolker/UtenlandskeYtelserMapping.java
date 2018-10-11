package no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.UtenlandskeYtelser;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Bolk;

import java.util.ArrayList;
import java.util.Map;

import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Tekstnokkel.*;

public class UtenlandskeYtelserMapping extends BolkMapping {
    public UtenlandskeYtelserMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad, Unleash unleash) {
        Bolk utenlandskeYtelserBolk = new Bolk();

        UtenlandskeYtelser utenlandskeYtelser = soknad.utenlandskeYtelser;
        utenlandskeYtelserBolk.tittel = tekster.get(UTENLANDSKE_YTELSER_TITTEL.getNokkel());
        utenlandskeYtelserBolk.elementer = new ArrayList<>();

        if ("NEI".equalsIgnoreCase(utenlandskeYtelser.mottarYtelserFraUtland)) {
            utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND.getNokkel(), SVAR_NEI.getNokkel()));
        } else {
            utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND.getNokkel(), SVAR_JA.getNokkel()));
            utenlandskeYtelserBolk.elementer.add(nyttElementMedVerdisvar.apply(UTENLANDSKE_YTELSER_FORKLARING.getNokkel(), utenlandskeYtelser.mottarYtelserFraUtlandForklaring));
        }

        if ("JA".equalsIgnoreCase(soknad.familieforhold.borForeldreneSammenMedBarnet)) {
            if ("NEI".equalsIgnoreCase(utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland)) {
                utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND.getNokkel(), SVAR_NEI.getNokkel()));
            } else {
                utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND.getNokkel(), SVAR_JA.getNokkel()));
                utenlandskeYtelserBolk.elementer.add(nyttElementMedVerdisvar.apply(UTENLANDSKE_YTELSER_FORKLARING.getNokkel(), utenlandskeYtelser.mottarAnnenForelderYtelserFraUtlandForklaring));
            }
        }

        return utenlandskeYtelserBolk;
    }
}
