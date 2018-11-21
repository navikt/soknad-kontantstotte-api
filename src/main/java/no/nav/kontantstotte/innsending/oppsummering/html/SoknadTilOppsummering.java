package no.nav.kontantstotte.innsending.oppsummering.html;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.mapping.*;
import no.nav.kontantstotte.tekst.TekstProvider;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    private final TekstProvider tekstProvider;

    public SoknadTilOppsummering(TekstProvider tekstProvider) {
        this.tekstProvider = tekstProvider;
    }

    public SoknadOppsummering map(Soknad soknad, String fnr) {
        Map<String, String> tekster = tekstProvider.hentTekster(soknad.getSprak());
        return new SoknadOppsummering(
                fnr,
                FORMATTER.format(soknad.getInnsendingsTidspunkt()),
                mapBolker(soknad, tekster),
                tekster);
    }

    private List<Bolk> mapBolker(Soknad soknad, Map<String, String> tekster) {
        return Arrays.asList(
                new KravTilSokerMapping(tekster).map(soknad),
                new BarnMapping(tekster).map(soknad),
                new BarnehageplassMapping(tekster).map(soknad),
                new FamilieforholdMapping(tekster).map(soknad),
                new TilknytningTilUtlandMapping(tekster).map(soknad),
                new ArbeidIUtlandetMapping(tekster).map(soknad),
                new UtenlandskeYtelserMapping(tekster).map(soknad),
                new UtenlandskKontantstotteMapping(tekster).map(soknad)
        );
    }

    private Bolk nyBolk(String bolknavn) {
        Bolk bolk = new Bolk();
        bolk.bolknavn = bolknavn;
        return bolk;
    }
}
