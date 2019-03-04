package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.SvarElement;
import no.nav.kontantstotte.innsending.steg.TilknytningTilUtland;

import java.util.ArrayList;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class TilknytningTilUtlandMapping extends BolkMapping {
    public TilknytningTilUtlandMapping(Tekster tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        Bolk tilknytningTilUtlandBolk = new Bolk();

        TilknytningTilUtland tilknytningTilUtland = soknad.tilknytningTilUtland;
        tilknytningTilUtlandBolk.tittel = tekster.hentTekst(TILKNYTNING_TIL_UTLAND_TITTEL.getNokkel());
        tilknytningTilUtlandBolk.elementer = new ArrayList<>();

        String sokerBoddEllerJobbetINorgeMinstFemAar = tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar != null ?
                tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar.toUpperCase() : "";
        String annenForelderBoddEllerJobbetINorgeMinstFemAar = tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar != null ?
                tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar.toUpperCase() : "";

        switch(sokerBoddEllerJobbetINorgeMinstFemAar){
            case "JAINORGE":
                tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, SVAR_JA_I_NORGE));
                break;
            case "JAIEOS":
                tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, SVAR_JA_I_EOS));
                tilknytningTilUtlandBolk.elementer.add(nyttElementMedVerdisvar.apply(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAarForklaring));
                break;
            case "JALEGGERSAMMENPERIODEREOS":
                tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, SOKER_SVAR_JA_LEGGER_SAMMEN_PERIODER_EOS));
                tilknytningTilUtlandBolk.elementer.add(nyttElementMedVerdisvar.apply(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAarForklaring));
                break;
            case "NEI":
                tilknytningTilUtlandBolk.elementer.add(
                                SvarElement.nyttSvar(
                                        tekster.hentTekst(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR.getNokkel()),
                                        tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar,
                                        tekster.hentTekst(TILKNYTNING_TIL_UTLAND_SOKER_IKKE_BODD_I_NORGE_ADVARSEL.getNokkel())
                                )
                );
                break;
            default:
                break;
        }

        if ("JA".equalsIgnoreCase(soknad.familieforhold.borForeldreneSammenMedBarnet)) {

            switch(annenForelderBoddEllerJobbetINorgeMinstFemAar){
                case "JAINORGE":
                    tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, SVAR_JA_I_NORGE));
                    break;
                case "JAIEOS":
                    tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, SVAR_JA_I_EOS));
                    tilknytningTilUtlandBolk.elementer.add(nyttElementMedVerdisvar.apply(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAarForklaring));
                    break;
                case "JALEGGERSAMMENPERIODEREOS":
                    tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, ANNEN_FORELDER_SVAR_JA_LEGGER_SAMMEN_PERIODER_EOS));
                    tilknytningTilUtlandBolk.elementer.add(nyttElementMedVerdisvar.apply(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAarForklaring));
                    break;
                case "NEI":
                    tilknytningTilUtlandBolk.elementer.add(
                                    SvarElement.nyttSvar(
                                            tekster.hentTekst(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER.getNokkel()),
                                            tekster.hentTekst(TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE.getNokkel()),
                                            tekster.hentTekst(TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE_ADVARSEL.getNokkel())
                                    )
                    );
                    break;
                default:
                    break;
            }
        }

        return tilknytningTilUtlandBolk;
    }

}
