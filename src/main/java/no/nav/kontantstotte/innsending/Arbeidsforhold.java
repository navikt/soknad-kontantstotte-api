package no.nav.kontantstotte.innsending;

import java.util.Optional;

public class Arbeidsforhold {

    public String arbeiderIUtlandetEllerKontinentalsokkel;
    public String mottarKontantstotteFraAnnetEOS;
    public String mottarYtelserFraUtlandet;
    public String arbeiderIUtlandetEllerKontinentalsokkelForklaring;
    public String mottarKontantstotteFraAnnetEOSForklaring;
    public String mottarYtelserFraUtlandetForklaring;

    public Optional<String> getArbeiderIUtlandetEllerKontinentalsokkelForklaring() {
        return Optional.ofNullable(arbeiderIUtlandetEllerKontinentalsokkelForklaring);
    }

    public Optional<String> getMottarKontantstotteFraAnnetEOSForklaring() {
        return Optional.ofNullable(mottarKontantstotteFraAnnetEOSForklaring);
    }

    public Optional<String> getMottarYtelserFraUtlandetForklaring() {
        return Optional.ofNullable(mottarYtelserFraUtlandetForklaring);
    }
}
