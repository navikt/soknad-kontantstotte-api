package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;


import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.BarnMapping;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.BarnehageplassMapping;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.FamilieforholdMapping;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Klassen benyttes til ny pdf generering.
 * mapBolker tar inn søknadsobjektet og mapper til ny
 * oppsummeringsobjekt som skal til ny html+pdf generator.
 *
 * For hver bolk man tar fra soknad til ny oppsummering skriver vi en map
 * funksjon som erstatter det gamle attributtet.
 */
public class SoknadTilOppsummering {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH.mm")
            .withZone(ZoneId.of("Europe/Paris"));

    private final Unleash unleash;

    public SoknadTilOppsummering(Unleash unleash) {
        this.unleash = unleash;
    }

    public SoknadOppsummering map(Soknad soknad, Map<String, String> tekster, String fnr) {
        return new SoknadOppsummering(soknad,
                fnr,
                FORMATTER.format(soknad.innsendingsTidspunkt),
                mapBolker(soknad, tekster),
                tekster);
    }

    private List<Bolk> mapBolker(Soknad soknad, Map<String, String> tekster) {

        return Arrays.asList(
                nyBolk("kravTilSoker"),
                new BarnMapping(tekster).map(soknad, unleash),
                new BarnehageplassMapping(tekster).map(soknad, unleash),
                new FamilieforholdMapping(tekster).map(soknad, unleash),
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
