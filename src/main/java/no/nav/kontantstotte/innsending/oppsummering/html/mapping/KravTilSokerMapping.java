package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;

import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class KravTilSokerMapping extends BolkMapping {
    public KravTilSokerMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {

        return new Bolk()
                .medTittel(tekst(KRAV_TIL_SOKER_TITTEL))
                .add(svar(KRAV_TIL_SOKER_NORSK_STATSBORGER))
                .add(svar(KRAV_TIL_SOKER_BODD_ELLER_JOBBET_I_NORGE_SISTE_FEM_AAR))
                .add(svar(KRAV_TIL_SOKER_BOR_SAMMEN_MED_BARNET))
                .add(svar(KRAV_TIL_SOKER_BARN_IKKE_HJEMME))
                .add(svar(KRAV_TIL_SOKER_IKKE_AVTALT_DELT_BOSTED))
                .add(svar(KRAV_TIL_SOKER_SKAL_BO_MED_BARNET_I_NORGE_NESTE_TOLV_MAANEDER));
    }
}
