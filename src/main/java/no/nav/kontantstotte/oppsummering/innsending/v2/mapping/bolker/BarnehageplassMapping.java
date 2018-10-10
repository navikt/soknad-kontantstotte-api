package no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barnehageplass;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Bolk;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Element;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;

public class BarnehageplassMapping implements BolkMapping {
    public static final String BARNEHAGEPLASS_TITTEL = "barnehageplass.tittel";
    public static final String HAR_BARNEHAGEPLASS = "oppsummering.barnehageplass.harBarnehageplass";
    public static final String BARN_BARNEHAGEPLASS_STATUS = "barnehageplass.barnBarnehageplassStatus";
    public static final String BARNEHAGEPLASS_HOYT_TIMEANTALL_ADVARSEL = "advarsel.barnehageplass.timerIBarnehage";

    @Override
    public Bolk map(Soknad soknad, Map<String, String> tekster, Unleash unleash) {

        BiFunction<String, String, Element> nyttElementMedTekstsvar = SoknadTilOppsummering.opprettElementMedTekster(tekster);
        BiFunction<String, String, Element> nyttElementMedVerdisvar = SoknadTilOppsummering.opprettElementMedVerdier(tekster);

        Bolk barnehageplassBolk = new Bolk();
        Barnehageplass barnehageplass = soknad.barnehageplass;

        barnehageplassBolk.tittel = tekster.get(BARNEHAGEPLASS_TITTEL);
        barnehageplassBolk.elementer = new ArrayList<>();

        barnehageplassBolk.elementer.add(Element.nyttSvar(tekster.get(HAR_BARNEHAGEPLASS), barnehageplass.harBarnehageplass));

        /*
        if (barnehageplass.barnBarnehageplassStatus != null) {
            barnehageplassBolk.elementer.add(nyttElementMedTekstsvar.apply(BARN_BARNEHAGEPLASS_STATUS, barnehageplass.barnBarnehageplassStatus.getKeyTekstNokkel()));


            switch (barnehageplass.barnBarnehageplassStatus) {
                case harSluttetIBarnehage:
                    svar = Arrays.asList(
                            nyttElementMedVerdisvar.apply("barnehageplass.harBarnehageplass.dato.sporsmal", barnehageplass.harSluttetIBarnehageDato)
                            , barnehageplass.harSluttetIBarnehageAntallTimer, barnehageplass.harSluttetIBarnehageKommune);
                    barnehageplassBolk.elementer.addAll(leggTilUtvidetInfoElementer(sporsmal, svar));
                    break;
                case skalSlutteIBarnehage:
                    svar = Arrays.asList(barnehageplass.skalSlutteIBarnehageDato, barnehageplass.skalSlutteIBarnehageAntallTimer, barnehageplass.skalSlutteIBarnehageKommune);
                    barnehageplassBolk.elementer.addAll(leggTilUtvidetInfoElementer(sporsmal, svar));
                    break;
                case harBarnehageplass:
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmal.get(0), barnehageplass.harBarnehageplassDato));
                    if (Integer.parseInt(barnehageplass.harBarnehageplassAntallTimer) > 33 && unleash.isEnabled(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL)) {
                        barnehageplassBolk.elementer.add(
                                Element.nyttSvar(sporsmal.get(1), barnehageplass.harBarnehageplassAntallTimer, tekster.get(BARNEHAGEPLASS_HOYT_TIMEANTALL_ADVARSEL))
                        );
                    } else {
                        barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmal.get(1), barnehageplass.harBarnehageplassAntallTimer));
                    }
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmal.get(2), barnehageplass.harBarnehageplassKommune));
                    break;
                case skalBegynneIBarnehage:
                    svar = Arrays.asList(barnehageplass.skalBegynneIBarnehageDato, barnehageplass.skalBegynneIBarnehageAntallTimer, barnehageplass.skalBegynneIBarnehageKommune);
                    barnehageplassBolk.elementer.addAll(leggTilUtvidetInfoElementer(sporsmal, svar));
                    break;
            }
        }*/

        return barnehageplassBolk;
    }
}
