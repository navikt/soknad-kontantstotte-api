package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;


import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barn;
import no.nav.kontantstotte.oppsummering.bolk.Barnehageplass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;

/**
 * Klassen benyttes til ny pdf generering.
 * mapBolker tar inn søknadsobjektet og mapper til ny
 * oppsummeringsobjekt som skal til ny html+pdf generator.
 *
 * For hver bolk man tar fra soknad til ny oppsummering skriver vi en map
 * funksjon som erstatter det gamle attributtet.
 */
public class SoknadTilOppsummering {
    public static final String BARN_TITTEL = "barn.tittel";
    public static final String BARN_UNDERTITTEL = "oppsummering.barn.subtittel";
    public static final String BARN_NAVN = "barn.navn";
    public static final String BARN_FODSELSDATO = "barn.fodselsdato";

    public static final String BARNEHAGEPLASS_TITTEL = "barnehageplass.tittel";
    public static final String HAR_BARNEHAGEPLASS = "oppsummering.barnehageplass.harBarnehageplass";
    public static final String BARN_BARNEHAGEPLASS_STATUS = "barnehageplass.barnBarnehageplassStatus";
    public static final String BARNEHAGEPLASS_HOYT_TIMEANTALL_ADVARSEL = "advarsel.barnehageplass.timerIBarnehage";


    public SoknadOppsummering map(Soknad soknad, Map<String, String> tekster, String fnr, Unleash unleash) {
        return new SoknadOppsummering(soknad,
                fnr,
                mapBolker(soknad, tekster, unleash),
                tekster);
    }

    private List<Bolk> mapBolker(Soknad soknad, Map<String, String> tekster, Unleash unleash) {
        return Arrays.asList(
                nyBolk("kravTilSoker"),
                mapBarn(soknad.mineBarn, tekster),
                mapBarnehageplass(soknad.barnehageplass, tekster, unleash),
                nyBolk("familieforhold"),
                nyBolk("tilknytningTilUtland"),
                nyBolk("arbeidIUtlandet"),
                nyBolk("utenlandskeYtelser"),
                nyBolk("utenlandskKontantstotte"),
                nyBolk("oppsummering")
        );
    }

    private Bolk nyBolk(String bolknavn) {
        Bolk bolk = new Bolk();
        bolk.bolknavn = bolknavn;
        return bolk;
    }

    public Bolk mapBarn(Barn barn, Map<String, String> tekster) {
        Bolk barneBolk = new Bolk();
        barneBolk.tittel = tekster.get(BARN_TITTEL);
        barneBolk.undertittel = tekster.get(BARN_UNDERTITTEL);
        barneBolk.elementer = new ArrayList<>();
        barneBolk.elementer.add(Element.nyttSvar(tekster.get(BARN_NAVN), barn.navn));
        barneBolk.elementer.add(Element.nyttSvar(tekster.get(BARN_FODSELSDATO), barn.fodselsdato));
        return barneBolk;
    }

    public Bolk mapBarnehageplass(Barnehageplass barnehageplass, Map<String, String> tekster, Unleash unleash) {
        Bolk barnehageplassBolk = new Bolk();
        barnehageplassBolk.tittel = tekster.get(BARNEHAGEPLASS_TITTEL);
        barnehageplassBolk.elementer = new ArrayList<>();

        barnehageplassBolk.elementer.add(Element.nyttSvar(tekster.get(HAR_BARNEHAGEPLASS), barnehageplass.harBarnehageplass));

        if (barnehageplass.barnBarnehageplassStatus != null) {
            barnehageplassBolk.elementer.add(Element.nyttSvar(tekster.get(BARN_BARNEHAGEPLASS_STATUS), tekster.get(barnehageplass.barnBarnehageplassStatus.getTekstNokkel())));

            List<String> sporsmalNokler = barnehageplass.barnBarnehageplassStatus.getSporsmalNokler().stream().map(tekster::get).collect(Collectors.toList());
            switch (barnehageplass.barnBarnehageplassStatus) {
                case harSluttetIBarnehage:
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(0), barnehageplass.harSluttetIBarnehageDato));
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(1), barnehageplass.harSluttetIBarnehageAntallTimer));
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(2), barnehageplass.harSluttetIBarnehageKommune));
                    break;
                case skalSlutteIBarnehage:
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(0), barnehageplass.skalSlutteIBarnehageDato));
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(1), barnehageplass.skalSlutteIBarnehageAntallTimer));
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(2), barnehageplass.skalSlutteIBarnehageKommune));
                    break;
                case harBarnehageplass:
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(0), barnehageplass.harBarnehageplassDato));
                    if (Integer.parseInt(barnehageplass.harBarnehageplassAntallTimer) > 33 && unleash.isEnabled(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL)) {
                        barnehageplassBolk.elementer.add(
                                Element.nyttSvar(sporsmalNokler.get(1), barnehageplass.harBarnehageplassAntallTimer, tekster.get(BARNEHAGEPLASS_HOYT_TIMEANTALL_ADVARSEL))
                        );
                    } else {
                        barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(1), barnehageplass.harBarnehageplassAntallTimer));
                    }
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(2), barnehageplass.harBarnehageplassKommune));
                    break;
                case skalBegynneIBarnehage:
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(0), barnehageplass.skalBegynneIBarnehageDato));
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(1), barnehageplass.skalBegynneIBarnehageAntallTimer));
                    barnehageplassBolk.elementer.add(Element.nyttSvar(sporsmalNokler.get(2), barnehageplass.skalBegynneIBarnehageKommune));
                    break;
            }
        }

        return barnehageplassBolk;
    }
}
