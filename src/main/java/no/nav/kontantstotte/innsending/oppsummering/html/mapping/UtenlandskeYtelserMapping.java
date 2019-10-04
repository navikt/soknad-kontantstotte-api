package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.familie.ks.kontrakter.søknad.AktørArbeidYtelseUtland;
import no.nav.familie.ks.kontrakter.søknad.Standpunkt;
import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.steg.UtenlandskeYtelser;

import java.util.ArrayList;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class UtenlandskeYtelserMapping extends BolkMapping {
    public UtenlandskeYtelserMapping(Tekster tekster) {
        super(tekster);
    }


    @Override
    public Bolk map(Soknad soknad) {
        Bolk utenlandskeYtelserBolk = new Bolk();

        UtenlandskeYtelser utenlandskeYtelser = soknad.utenlandskeYtelser;
        utenlandskeYtelserBolk.tittel = tekster.hentTekst(UTENLANDSKE_YTELSER_TITTEL.getNokkel());
        utenlandskeYtelserBolk.elementer = new ArrayList<>();

        if ("NEI".equalsIgnoreCase(utenlandskeYtelser.mottarYtelserFraUtland)) {
            utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND, SVAR_NEI));
        } else {
            utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND, SVAR_JA));
            utenlandskeYtelserBolk.elementer.add(nyttElementMedVerdisvar.apply(UTENLANDSKE_YTELSER_FORKLARING, utenlandskeYtelser.mottarYtelserFraUtlandForklaring));
        }

        if ("JA".equalsIgnoreCase(soknad.familieforhold.borForeldreneSammenMedBarnet)) {
            if ("NEI".equalsIgnoreCase(utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland)) {
                utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND, SVAR_NEI));
            } else {
                utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND, SVAR_JA));
                utenlandskeYtelserBolk.elementer.add(nyttElementMedVerdisvar.apply(UTENLANDSKE_YTELSER_FORKLARING, utenlandskeYtelser.mottarAnnenForelderYtelserFraUtlandForklaring));
            }
        }

        return utenlandskeYtelserBolk;
    }

    public Bolk mapNy(Søknad søknad) {
        Bolk utenlandskeYtelserBolk = new Bolk();
        utenlandskeYtelserBolk.tittel = tekster.hentTekst(UTENLANDSKE_YTELSER_TITTEL.getNokkel());
        utenlandskeYtelserBolk.elementer = new ArrayList<>();

        AktørArbeidYtelseUtland søkerYtelserUtland = MappingUtils.hentArbeidYtelseUtlandForSøker(søknad);
        genererUtenlandskeYtelserElementer(utenlandskeYtelserBolk, søkerYtelserUtland, UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND);

        if (søknad.getOppgittUtlandsTilknytning().getAktørerArbeidYtelseIUtlandet().size() > 1) {
            AktørArbeidYtelseUtland medForelderYtelserUtland = MappingUtils.hentArbeidYtelseUtlandForAnnenPart(søknad);
            genererUtenlandskeYtelserElementer(utenlandskeYtelserBolk, medForelderYtelserUtland, UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND);
        }

        return utenlandskeYtelserBolk;
    }

    private void genererUtenlandskeYtelserElementer(Bolk utenlandskeYtelserBolk, AktørArbeidYtelseUtland ytelserUtland, Tekstnokkel ytelserUtlandTekstnøkkel) {
        if (Standpunkt.NEI.equals(ytelserUtland.getYtelseIUtlandet())) {
            utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(ytelserUtlandTekstnøkkel, SVAR_NEI));
        } else if (Standpunkt.JA.equals(ytelserUtland.getYtelseIUtlandet())) {
            utenlandskeYtelserBolk.elementer.add(nyttElementMedTekstsvar.apply(ytelserUtlandTekstnøkkel, SVAR_JA));
            utenlandskeYtelserBolk.elementer.add(nyttElementMedVerdisvar.apply(UTENLANDSKE_YTELSER_FORKLARING, ytelserUtland.getYtelseIUtlandetForklaring()));
        }
    }
}
