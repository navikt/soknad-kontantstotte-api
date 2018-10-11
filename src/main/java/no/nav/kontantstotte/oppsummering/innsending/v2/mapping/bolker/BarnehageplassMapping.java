package no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barnehageplass;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Bolk;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Tekstnokkel.*;

public class BarnehageplassMapping extends BolkMapping {
    public BarnehageplassMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad, Unleash unleash) {
        Bolk barnehageplassBolk = new Bolk();
        Barnehageplass barnehageplass = soknad.barnehageplass;

        barnehageplassBolk.tittel = tekster.get(BARNEHAGEPLASS_TITTEL.getNokkel());
        barnehageplassBolk.elementer = new ArrayList<>();

        if("NEI".equalsIgnoreCase(barnehageplass.harBarnehageplass)){
            barnehageplassBolk.elementer.add(nyttElementMedTekstsvar.apply(HAR_BARNEHAGEPLASS, SVAR_NEI));
        } else {
            barnehageplassBolk.elementer.add(nyttElementMedTekstsvar.apply(HAR_BARNEHAGEPLASS, SVAR_JA));
        }

        if (barnehageplass.barnBarnehageplassStatus != null) {
            barnehageplassBolk.elementer.add(nyttElementMedTekstsvar.apply(BARN_BARNEHAGEPLASS_STATUS, barnehageplass.barnBarnehageplassStatus.getTekstNokkel()));

            switch (barnehageplass.barnBarnehageplassStatus) {
                case harSluttetIBarnehage:
                    barnehageplassBolk.elementer.addAll(
                        Arrays.asList(
                            nyttElementMedVerdisvar.apply(HAR_SLUTTET_I_BARNEHAGE_DATO, barnehageplass.harSluttetIBarnehageDato),
                            nyttElementMedVerdisvar.apply(HAR_SLUTTET_I_BARNEHAGE_ANTALL_TIMER, barnehageplass.harSluttetIBarnehageAntallTimer),
                            nyttElementMedVerdisvar.apply(HAR_SLUTTET_I_BARNEHAGE_KOMMUNE, barnehageplass.harSluttetIBarnehageKommune)
                        )
                    );
                    break;
                case skalSlutteIBarnehage:
                    barnehageplassBolk.elementer.addAll(
                        Arrays.asList(
                            nyttElementMedVerdisvar.apply(SKAL_SLUTTE_I_BARNEHAGE_DATO, barnehageplass.skalSlutteIBarnehageDato),
                            nyttElementMedVerdisvar.apply(SKAL_SLUTTE_I_BARNEHAGE_ANTALL_TIMER, barnehageplass.skalSlutteIBarnehageAntallTimer),
                            nyttElementMedVerdisvar.apply(SKAL_SLUTTE_I_BARNEHAGE_KOMMUNE, barnehageplass.skalSlutteIBarnehageKommune)
                        )
                    );
                    break;
                case harBarnehageplass:
                    Element harBarnehageplassAntallTimer = Integer.parseInt(barnehageplass.harBarnehageplassAntallTimer) > 33 && unleash.isEnabled(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL) ?
                            Element.nyttSvar(
                                    tekster.get(HAR_BARNEHAGEPLASS_ANTALL_TIMER.getNokkel()),
                                    barnehageplass.harBarnehageplassAntallTimer,
                                    tekster.get(BARNEHAGEPLASS_HOYT_TIMEANTALL_ADVARSEL.getNokkel())
                            ) :
                            nyttElementMedVerdisvar.apply(HAR_BARNEHAGEPLASS_ANTALL_TIMER, barnehageplass.harBarnehageplassAntallTimer);

                    barnehageplassBolk.elementer.addAll(
                        Arrays.asList(
                            nyttElementMedVerdisvar.apply(HAR_BARNEHAGEPLASS_DATO, barnehageplass.harBarnehageplassDato),
                            harBarnehageplassAntallTimer,
                            nyttElementMedVerdisvar.apply(HAR_BARNEHAGEPLASS_KOMMUNE, barnehageplass.harBarnehageplassKommune)
                        )
                    );

                    break;
                case skalBegynneIBarnehage:
                    barnehageplassBolk.elementer.addAll(
                            Arrays.asList(
                                    nyttElementMedVerdisvar.apply(SKAL_BEGYNNE_I_BARNEHAGE_DATO, barnehageplass.skalBegynneIBarnehageDato),
                                    nyttElementMedVerdisvar.apply(SKAL_BEGYNNE_I_BARNEHAGE_ANTALL_TIMER, barnehageplass.skalBegynneIBarnehageAntallTimer),
                                    nyttElementMedVerdisvar.apply(SKAL_BEGYNNE_I_BARNEHAGE_KOMMUNE, barnehageplass.skalBegynneIBarnehageKommune)
                            )
                    );
                    break;
            }
        }

        return barnehageplassBolk;
    }
}
