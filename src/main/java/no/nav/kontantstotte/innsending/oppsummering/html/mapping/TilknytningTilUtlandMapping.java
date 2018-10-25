package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.TilknytningTilUtland;

import java.util.ArrayList;
import java.util.Map;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class TilknytningTilUtlandMapping extends BolkMapping {
    public TilknytningTilUtlandMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        Bolk tilknytningTilUtlandBolk = new Bolk();

        TilknytningTilUtland tilknytningTilUtland = soknad.tilknytningTilUtland;
        tilknytningTilUtlandBolk.tittel = tekster.get(TILKNYTNING_TIL_UTLAND_TITTEL.getNokkel());
        tilknytningTilUtlandBolk.elementer = new ArrayList<>();

        if("JAINORGE".equalsIgnoreCase(tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar)) {
            tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, SVAR_JA_I_NORGE));
        } else if("JAIEOS".equalsIgnoreCase(tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar)) {
            tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, SVAR_JA_I_EOS));
            tilknytningTilUtlandBolk.elementer.add(nyttElementMedVerdisvar.apply(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAarForklaring));
        } else if("JALEGGERSAMMENPERIODEREOS".equalsIgnoreCase(tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar)) {
            tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, SOKER_SVAR_JA_LEGGER_SAMMEN_PERIODER_EOS));
            tilknytningTilUtlandBolk.elementer.add(nyttElementMedVerdisvar.apply(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAarForklaring));
        }
        else {
            tilknytningTilUtlandBolk.elementer.add(
                    UnleashProvider.get().isEnabled(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL) ?
                            Element.nyttSvar(
                                    tekster.get(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR.getNokkel()),
                                    tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar,
                                    tekster.get(TILKNYTNING_TIL_UTLAND_SOKER_IKKE_BODD_I_NORGE_ADVARSEL.getNokkel())
                            ) :
                            nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR, TILKNYTNING_TIL_UTLAND_SOKER_IKKE_BODD_I_NORGE)
            );
        }

        if ("JA".equalsIgnoreCase(soknad.familieforhold.borForeldreneSammenMedBarnet)) {
            if("JAINORGE".equalsIgnoreCase(tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar)) {
                tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, SVAR_JA_I_NORGE));
            } else if("JAIEOS".equalsIgnoreCase(tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar)) {
                tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, SVAR_JA_I_EOS));
                tilknytningTilUtlandBolk.elementer.add(nyttElementMedVerdisvar.apply(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAarForklaring));
            } else if("JALEGGERSAMMENPERIODEREOS".equalsIgnoreCase(tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar)) {
                tilknytningTilUtlandBolk.elementer.add(nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, ANNEN_FORELDER_SVAR_JA_LEGGER_SAMMEN_PERIODER_EOS));
                tilknytningTilUtlandBolk.elementer.add(nyttElementMedVerdisvar.apply(TILKNYTNING_TIL_UTLAND_FORKLARING, tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAarForklaring));
            } else if("NEI".equalsIgnoreCase(tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar)) {
                tilknytningTilUtlandBolk.elementer.add(
                        UnleashProvider.get().isEnabled(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL) ?
                                Element.nyttSvar(
                                        tekster.get(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER.getNokkel()),
                                        tekster.get(TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE.getNokkel()),
                                        tekster.get(TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE_ADVARSEL.getNokkel())
                                ) :
                                nyttElementMedTekstsvar.apply(TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER, TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE)
                );
            }
        }

        return tilknytningTilUtlandBolk;
    }

}
