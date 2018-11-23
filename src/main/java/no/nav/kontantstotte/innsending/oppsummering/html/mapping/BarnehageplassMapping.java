package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Barnehageplass;

import java.util.Map;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class BarnehageplassMapping extends BolkMapping {
    public BarnehageplassMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        Barnehageplass barnehageplass = soknad.getBarnehageplass();

        Bolk bolk = new Bolk()
                .medTittel(tekst(BARNEHAGEPLASS_TITTEL))
                .add(svar(HAR_BARNEHAGEPLASS, barnehageplass.harBarnehageplass ? SVAR_JA : SVAR_NEI));

        if (barnehageplass.barnBarnehageplassStatus != null) {
            bolk.add(svar(BARN_BARNEHAGEPLASS_STATUS, barnehageplass.barnBarnehageplassStatus.getTekstNokkel()));

            switch (barnehageplass.barnBarnehageplassStatus) {
                case harSluttetIBarnehage:
                    bolk
                            .add(svar(HAR_SLUTTET_I_BARNEHAGE_DATO, barnehageplass.harSluttetIBarnehageDato))
                            .add(svar(HAR_SLUTTET_I_BARNEHAGE_ANTALL_TIMER, barnehageplass.harSluttetIBarnehageAntallTimer))
                            .add(svar(HAR_SLUTTET_I_BARNEHAGE_KOMMUNE, barnehageplass.harSluttetIBarnehageKommune));
                    break;
                case skalSlutteIBarnehage:
                    bolk
                            .add(svar(SKAL_SLUTTE_I_BARNEHAGE_DATO, barnehageplass.skalSlutteIBarnehageDato))
                            .add(svar(SKAL_SLUTTE_I_BARNEHAGE_ANTALL_TIMER, barnehageplass.skalSlutteIBarnehageAntallTimer))
                            .add(svar(SKAL_SLUTTE_I_BARNEHAGE_KOMMUNE, barnehageplass.skalSlutteIBarnehageKommune));
                    break;
                case harBarnehageplass:
                    Element harBarnehageplassAntallTimer = barnehageplass.harBarnehageplassAntallTimer > 33 && UnleashProvider.get().isEnabled(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL) ?
                            svar(
                                    HAR_BARNEHAGEPLASS_ANTALL_TIMER,
                                    barnehageplass.harBarnehageplassAntallTimer,
                                    BARNEHAGEPLASS_HOYT_TIMEANTALL_ADVARSEL) :
                            svar(HAR_BARNEHAGEPLASS_ANTALL_TIMER, barnehageplass.harBarnehageplassAntallTimer);

                    bolk
                            .add(svar(HAR_BARNEHAGEPLASS_DATO, barnehageplass.harBarnehageplassDato))
                            .add(harBarnehageplassAntallTimer)
                            .add(svar(HAR_BARNEHAGEPLASS_KOMMUNE, barnehageplass.harBarnehageplassKommune));

                    break;
                case skalBegynneIBarnehage:
                    bolk
                            .add(svar(SKAL_BEGYNNE_I_BARNEHAGE_DATO, barnehageplass.skalBegynneIBarnehageDato))
                            .add(svar(SKAL_BEGYNNE_I_BARNEHAGE_ANTALL_TIMER, barnehageplass.skalBegynneIBarnehageAntallTimer))
                            .add(svar(SKAL_BEGYNNE_I_BARNEHAGE_KOMMUNE, barnehageplass.skalBegynneIBarnehageKommune));
                    break;
            }
        }

        return bolk;
    }

    static class BarnehageplassStatusMapper {

    }
}
