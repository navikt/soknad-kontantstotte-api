package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.steg.ArbeidIUtlandet;

import java.util.ArrayList;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class ArbeidIUtlandetMapping extends BolkMapping {
    public ArbeidIUtlandetMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        Bolk arbeidIUtlandetBolk = new Bolk();

        ArbeidIUtlandet arbeidIUtlandet = soknad.arbeidIUtlandet;
        arbeidIUtlandetBolk.tittel = tekster.get(ARBEID_I_UTLANDET_TITTEL.getNokkel());
        arbeidIUtlandetBolk.elementer = new ArrayList<>();

        if ("NEI".equalsIgnoreCase(arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkel)) {
            arbeidIUtlandetBolk.elementer.add(nyttElementMedTekstsvar.apply(ARBEID_I_UTLANDET_ELLER_KONTINENTALSOKKEL, SVAR_NEI));
        } else {
            arbeidIUtlandetBolk.elementer.add(nyttElementMedTekstsvar.apply(ARBEID_I_UTLANDET_ELLER_KONTINENTALSOKKEL, SVAR_JA));
            arbeidIUtlandetBolk.elementer.add(nyttElementMedVerdisvar.apply(ARBEID_I_UTLANDET_FORKLARING, arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkelForklaring));
        }

        if ("JA".equalsIgnoreCase(soknad.familieforhold.borForeldreneSammenMedBarnet)) {
            if ("NEI".equalsIgnoreCase(arbeidIUtlandet.arbeiderAnnenForelderIUtlandet)) {
                arbeidIUtlandetBolk.elementer.add(nyttElementMedTekstsvar.apply(ARBEID_I_UTLANDET_ARBEIDER_ANNEN_FORELDER_I_UTLANDET, SVAR_NEI));
            } else {
                arbeidIUtlandetBolk.elementer.add(nyttElementMedTekstsvar.apply(ARBEID_I_UTLANDET_ARBEIDER_ANNEN_FORELDER_I_UTLANDET, SVAR_JA));
                arbeidIUtlandetBolk.elementer.add(nyttElementMedVerdisvar.apply(ARBEID_I_UTLANDET_FORKLARING, arbeidIUtlandet.arbeiderAnnenForelderIUtlandetForklaring));
            }
        }

        return arbeidIUtlandetBolk;
    }
}
