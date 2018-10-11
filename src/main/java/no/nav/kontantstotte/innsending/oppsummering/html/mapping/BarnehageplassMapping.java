package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.steg.Barnehageplass;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static no.nav.kontantstotte.innsending.oppsummering.html.Tekstnokkel.*;

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
            barnehageplassBolk.elementer.add(nyttElementMedTekstsvar.apply(HAR_BARNEHAGEPLASS.getNokkel(), SVAR_NEI.getNokkel()));
        } else {
            barnehageplassBolk.elementer.add(nyttElementMedTekstsvar.apply(HAR_BARNEHAGEPLASS.getNokkel(), SVAR_JA.getNokkel()));
        }

        if (barnehageplass.barnBarnehageplassStatus != null) {
            barnehageplassBolk.elementer.add(nyttElementMedTekstsvar.apply(BARN_BARNEHAGEPLASS_STATUS.getNokkel(), barnehageplass.barnBarnehageplassStatus.getTekstNokkel()));

            switch (barnehageplass.barnBarnehageplassStatus) {
                case harSluttetIBarnehage:
                    barnehageplassBolk.elementer.addAll(
                        Arrays.asList(
                            nyttElementMedVerdisvar.apply(HAR_SLUTTET_I_BARNEHAGE_DATO.getNokkel(), barnehageplass.harSluttetIBarnehageDato),
                            nyttElementMedVerdisvar.apply(HAR_SLUTTET_I_BARNEHAGE_ANTALL_TIMER.getNokkel(), barnehageplass.harSluttetIBarnehageAntallTimer),
                            nyttElementMedVerdisvar.apply(HAR_SLUTTET_I_BARNEHAGE_KOMMUNE.getNokkel(), barnehageplass.harSluttetIBarnehageKommune)
                        )
                    );
                    break;
                case skalSlutteIBarnehage:
                    barnehageplassBolk.elementer.addAll(
                        Arrays.asList(
                            nyttElementMedVerdisvar.apply(SKAL_SLUTTE_I_BARNEHAGE_DATO.getNokkel(), barnehageplass.skalSlutteIBarnehageDato),
                            nyttElementMedVerdisvar.apply(SKAL_SLUTTE_I_BARNEHAGE_ANTALL_TIMER.getNokkel(), barnehageplass.skalSlutteIBarnehageAntallTimer),
                            nyttElementMedVerdisvar.apply(SKAL_SLUTTE_I_BARNEHAGE_KOMMUNE.getNokkel(), barnehageplass.skalSlutteIBarnehageKommune)
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
                            nyttElementMedVerdisvar.apply(HAR_BARNEHAGEPLASS_ANTALL_TIMER.getNokkel(), barnehageplass.harBarnehageplassAntallTimer);

                    barnehageplassBolk.elementer.addAll(
                        Arrays.asList(
                            nyttElementMedVerdisvar.apply(HAR_BARNEHAGEPLASS_DATO.getNokkel(), barnehageplass.harBarnehageplassDato),
                            harBarnehageplassAntallTimer,
                            nyttElementMedVerdisvar.apply(HAR_BARNEHAGEPLASS_KOMMUNE.getNokkel(), barnehageplass.harBarnehageplassKommune)
                        )
                    );

                    break;
                case skalBegynneIBarnehage:
                    barnehageplassBolk.elementer.addAll(
                            Arrays.asList(
                                    nyttElementMedVerdisvar.apply(SKAL_BEGYNNE_I_BARNEHAGE_DATO.getNokkel(), barnehageplass.skalBegynneIBarnehageDato),
                                    nyttElementMedVerdisvar.apply(SKAL_BEGYNNE_I_BARNEHAGE_ANTALL_TIMER.getNokkel(), barnehageplass.skalBegynneIBarnehageAntallTimer),
                                    nyttElementMedVerdisvar.apply(SKAL_BEGYNNE_I_BARNEHAGE_KOMMUNE.getNokkel(), barnehageplass.skalBegynneIBarnehageKommune)
                            )
                    );
                    break;
            }
        }

        return barnehageplassBolk;
    }
}
