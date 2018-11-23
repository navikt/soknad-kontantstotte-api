package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.steg.ArbeidIUtlandet;

import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class ArbeidIUtlandetMapping extends BolkMapping {
    public ArbeidIUtlandetMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        Bolk bolk = new Bolk()
                .medTittel(tekst(ARBEID_I_UTLANDET_TITTEL));

        ArbeidIUtlandet arbeidIUtlandet = soknad.getArbeidIUtlandet();

        if (arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkel) {
            bolk.add(svar(ARBEID_I_UTLANDET_ELLER_KONTINENTALSOKKEL, SVAR_JA));
            bolk.add(svar(ARBEID_I_UTLANDET_FORKLARING, arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkelForklaring));
        } else {
            bolk.add(svar(ARBEID_I_UTLANDET_ELLER_KONTINENTALSOKKEL, SVAR_NEI));
        }

        if (soknad.getFamilieforhold().borForeldreneSammenMedBarnet) {
            if (arbeidIUtlandet.arbeiderAnnenForelderIUtlandet) {
                bolk.add(svar(ARBEID_I_UTLANDET_ARBEIDER_ANNEN_FORELDER_I_UTLANDET, SVAR_JA));
                bolk.add(svar(ARBEID_I_UTLANDET_FORKLARING, arbeidIUtlandet.arbeiderAnnenForelderIUtlandetForklaring));
            } else {
                bolk.add(svar(ARBEID_I_UTLANDET_ARBEIDER_ANNEN_FORELDER_I_UTLANDET, SVAR_NEI));
            }
        }

        return bolk;
    }
}
