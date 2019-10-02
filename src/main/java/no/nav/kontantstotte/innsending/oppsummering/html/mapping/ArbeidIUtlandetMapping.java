package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.familie.ks.kontrakter.søknad.AktørArbeidYtelseUtland;
import no.nav.familie.ks.kontrakter.søknad.Standpunkt;
import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.steg.ArbeidIUtlandet;

import java.util.ArrayList;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class ArbeidIUtlandetMapping extends BolkMapping {
    public ArbeidIUtlandetMapping(Tekster tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        Bolk arbeidIUtlandetBolk = new Bolk();

        ArbeidIUtlandet arbeidIUtlandet = soknad.arbeidIUtlandet;
        arbeidIUtlandetBolk.tittel = tekster.hentTekst(ARBEID_I_UTLANDET_TITTEL.getNokkel());
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

    public Bolk mapNy(Søknad søknad) {
        Bolk arbeidIUtlandetBolk = new Bolk();

        arbeidIUtlandetBolk.tittel = tekster.hentTekst(ARBEID_I_UTLANDET_TITTEL.getNokkel());
        arbeidIUtlandetBolk.elementer = new ArrayList<>();

        AktørArbeidYtelseUtland søkerArbeidIUtlandet = new ArrayList<>(søknad.getOppgittUtlandsTilknytning().getAktørerArbeidYtelseIUtlandet()).get(0);
        genererArbeidIUtlandetElement(arbeidIUtlandetBolk, søkerArbeidIUtlandet, ARBEID_I_UTLANDET_ELLER_KONTINENTALSOKKEL);

        if (søknad.getOppgittUtlandsTilknytning().getAktørerArbeidYtelseIUtlandet().size() > 1) {
            AktørArbeidYtelseUtland medForelderArbeidIUtlandet = new ArrayList<>(søknad.getOppgittUtlandsTilknytning().getAktørerArbeidYtelseIUtlandet()).get(1);
            genererArbeidIUtlandetElement(arbeidIUtlandetBolk, medForelderArbeidIUtlandet, ARBEID_I_UTLANDET_ARBEIDER_ANNEN_FORELDER_I_UTLANDET);
        }

        return arbeidIUtlandetBolk;
    }

    private void genererArbeidIUtlandetElement(Bolk arbeidIUtlandetBolk, AktørArbeidYtelseUtland arbeidIUtlandet, Tekstnokkel arbeidIUtlandetTekstnøkkel) {
        if (Standpunkt.NEI.equals(arbeidIUtlandet.getArbeidIUtlandet())) {
            arbeidIUtlandetBolk.elementer.add(nyttElementMedTekstsvar.apply(arbeidIUtlandetTekstnøkkel, SVAR_NEI));
        } else if (Standpunkt.JA.equals(arbeidIUtlandet.getArbeidIUtlandet())) {
            arbeidIUtlandetBolk.elementer.add(nyttElementMedTekstsvar.apply(arbeidIUtlandetTekstnøkkel, SVAR_JA));
            arbeidIUtlandetBolk.elementer.add(nyttElementMedVerdisvar.apply(ARBEID_I_UTLANDET_FORKLARING, arbeidIUtlandet.getArbeidIUtlandetForklaring()));
        }
    }
}
