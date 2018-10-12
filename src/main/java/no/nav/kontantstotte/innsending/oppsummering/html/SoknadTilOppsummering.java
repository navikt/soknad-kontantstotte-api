package no.nav.kontantstotte.innsending.oppsummering.html;


import no.finn.unleash.Unleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.mapping.*;
import no.nav.kontantstotte.tekst.TekstProvider;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Klassen benyttes til generering av oppsummeringsinnhold.
 * mapBolker tar inn soeknadsobjektet og mapper til nytt
 * oppsummeringsobjekt som skal til htmlconverter og deretter til pdfconverter.
 *
 * Maalet et er at SoknadOppsummering.java-objektet på sikt ikke skal inneholde soknad eller tekster,
 * men kun være bolker med spoersmal og svar og eventuell metadata tilknyttet oppsummeringen
 *
 * Naar metoden nyBolk ikke er i bruk mer, kan vi slette soknadsobjektet fra SoknadOppsummering og deretter
 * gjoere en del endringer i soknad-html-generator for å slette gamle react-filer og deretter ta grep for aa
 * slette eventuelle gjenvaerende tekster som er i bruk.
 *
 * Et steg i soknaden kan byttes ut med ny generering ved aa erstatte kallet til nyBolk med en ny mapper-klasse.
 * Husk aa endre i testene tilhoerende denne klassen og teste alle varianter i engen mapper
 *
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
                new UtenlandskeYtelserMapping(tekster).map(soknad, unleash),
                new UtenlandskKontantstotteMapping(tekster).map(soknad, unleash),
                nyBolk("oppsummering")
        );
    }

    private Bolk nyBolk(String bolknavn) {
        Bolk bolk = new Bolk();
        bolk.bolknavn = bolknavn;
        return bolk;
    }


}
