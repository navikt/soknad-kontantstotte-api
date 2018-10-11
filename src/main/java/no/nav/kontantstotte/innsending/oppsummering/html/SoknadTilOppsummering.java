package no.nav.kontantstotte.innsending.oppsummering.html;


import no.finn.unleash.Unleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.mapping.BarnMapping;
import no.nav.kontantstotte.innsending.oppsummering.html.mapping.BarnehageplassMapping;
import no.nav.kontantstotte.innsending.oppsummering.html.mapping.FamilieforholdMapping;
import no.nav.kontantstotte.tekst.TekstProvider;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Klassen benyttes til ny oppsummering generering.
 * mapBolker tar inn søknadsobjektet og mapper til ny
 * oppsummeringsobjekt som skal til ny html+oppsummering generator.
 * <p>
 * For hver steg man tar fra soknad til ny innsending skriver vi en map
 * funksjon som erstatter det gamle attributtet.
 */
class SoknadTilOppsummering {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH.mm")
            .withZone(ZoneId.of("Europe/Paris"));

    private final Unleash unleash;
    private final TekstProvider tekstProvider;

    public SoknadTilOppsummering(TekstProvider tekstProvider, Unleash unleash) {
        this.tekstProvider = tekstProvider;
        this.unleash = unleash;
    }

    public SoknadOppsummering map(Soknad soknad, String fnr) {
        Map<String, String> tekster = tekstProvider.hentTekster(soknad.sprak);
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


}
