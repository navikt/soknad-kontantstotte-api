package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.steg.TilknytningTilUtland;

import java.util.Map;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class TilknytningTilUtlandMapping extends BolkMapping {
    public TilknytningTilUtlandMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {

        TilknytningTilUtland tilknytningTilUtland = soknad.getTilknytningTilUtland();

        Bolk bolk = new Bolk()
                .medTittel(tekst(TILKNYTNING_TIL_UTLAND_TITTEL));

        String sokerBoddEllerJobbetINorgeMinstFemAar = tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar != null ?
                tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar.toUpperCase() : "";
        String annenForelderBoddEllerJobbetINorgeMinstFemAar = tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar != null ?
                tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar.toUpperCase() : "";

        switch(sokerBoddEllerJobbetINorgeMinstFemAar){
            case "JAINORGE":
                bolk.add(svar(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, SVAR_JA_I_NORGE));
                break;
            case "JAIEOS":
                bolk.add(svar(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, SVAR_JA_I_EOS));
                bolk.add(svar(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAarForklaring));
                break;
            case "JALEGGERSAMMENPERIODEREOS":
                bolk.add(svar(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, SOKER_SVAR_JA_LEGGER_SAMMEN_PERIODER_EOS));
                bolk.add(svar(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAarForklaring));
                break;
            case "NEI":
                bolk.add(
                        UnleashProvider.get().isEnabled(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL) ?
                                svar(
                                        TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR,
                                        tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar,
                                        TILKNYTNING_TIL_UTLAND_SOKER_IKKE_BODD_I_NORGE_ADVARSEL) :
                                svar(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, TILKNYTNING_TIL_UTLAND_SOKER_IKKE_BODD_I_NORGE)
                );
                break;
            default:
                break;
        }

        if (soknad.getFamilieforhold().borForeldreneSammenMedBarnet) {

            switch(annenForelderBoddEllerJobbetINorgeMinstFemAar){
                case "JAINORGE":
                    bolk.add(svar(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, SVAR_JA_I_NORGE));
                    break;
                case "JAIEOS":
                    bolk.add(svar(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, SVAR_JA_I_EOS));
                    bolk.add(svar(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAarForklaring));
                    break;
                case "JALEGGERSAMMENPERIODEREOS":
                    bolk.add(svar(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, ANNEN_FORELDER_SVAR_JA_LEGGER_SAMMEN_PERIODER_EOS));
                    bolk.add(svar(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAarForklaring));
                    break;
                case "NEI":
                    bolk.add(
                            UnleashProvider.get().isEnabled(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL) ?
                                    svar(
                                            TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER,
                                            TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE,
                                            TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE_ADVARSEL) :
                                    svar(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE)
                    );
                    break;
                default:
                    break;
            }
        }

        return bolk;
    }

}
