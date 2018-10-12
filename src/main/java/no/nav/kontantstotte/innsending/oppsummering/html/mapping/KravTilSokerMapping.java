package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class KravTilSokerMapping extends BolkMapping {
    public KravTilSokerMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad, Unleash unleash) {
        Bolk kravTilSokerBolk = new Bolk();

        kravTilSokerBolk.tittel = tekster.get(KRAV_TIL_SOKER_TITTEL.getNokkel());
        kravTilSokerBolk.elementer = new ArrayList<>();
        kravTilSokerBolk.elementer.addAll(
                Arrays.asList(
                        nyttElementMedSvar.apply(KRAV_TIL_SOKER_NORSK_STATSBORGER),
                        nyttElementMedSvar.apply(KRAV_TIL_SOKER_BODD_ELLER_JOBBET_I_NORGE_SISTE_FEM_AAR),
                        nyttElementMedSvar.apply(KRAV_TIL_SOKER_BOR_SAMMEN_MED_BARNET),
                        nyttElementMedSvar.apply(KRAV_TIL_SOKER_BARN_IKKE_HJEMME),
                        nyttElementMedSvar.apply(KRAV_TIL_SOKER_IKKE_AVTALT_DELT_BOSTED),
                        nyttElementMedSvar.apply(KRAV_TIL_SOKER_SKAL_BO_MED_BARNET_I_NORGE_NESTE_TOLV_MAANEDER)
                )
        );

        return kravTilSokerBolk;
    }
}
