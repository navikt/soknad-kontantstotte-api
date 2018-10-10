package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;


import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barn;
import no.nav.kontantstotte.oppsummering.bolk.Barnehageplass;
import no.nav.kontantstotte.oppsummering.bolk.Familieforhold;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.BarnMapping;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.BarnehageplassMapping;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.FamilieforholdMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
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
    public static final String SVAR_NEI = "svar.nei";
    public static final String SVAR_JA = "svar.ja";

    private final Unleash unleash;

    public SoknadTilOppsummering(Unleash unleash) {
        this.unleash = unleash;
    }

    public SoknadOppsummering map(Soknad soknad, Map<String, String> tekster, String fnr) {
        return new SoknadOppsummering(soknad,
                fnr,
                mapBolker(soknad, tekster),
                tekster);
    }

    private List<Bolk> mapBolker(Soknad soknad, Map<String, String> tekster) {
        return Arrays.asList(
                nyBolk("kravTilSoker"),
                new BarnMapping().map(soknad, tekster, unleash),
                new BarnehageplassMapping().map(soknad, tekster, unleash),
                new FamilieforholdMapping().map(soknad, tekster, unleash),
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

    public static BiFunction<String, String, Element> opprettElementMedTekster(Map<String, String> tekster){
        return (String sporsmal, String svar) -> Element.nyttSvar(tekster.get(sporsmal), tekster.get(svar));
    }

    public static BiFunction<String, String, Element> opprettElementMedVerdier(Map<String, String> tekster){
        return (String sporsmal, String svar) -> Element.nyttSvar(tekster.get(sporsmal), svar);
    }
}
