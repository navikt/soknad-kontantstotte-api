package no.nav.kontantstotte.innsending;

import java.util.Optional;

public class Arbeidsforhold {

    public String arbeiderIUtlandetEllerKontinentalsokkel;
    public String mottarKontantstotteFraAnnetEOS;
    public String mottarYtelserFraUtlandet;
    public String arbeiderIUtlandetEllerKontinentalsokkelForklaring;
    public String mottarKontantstotteFraAnnetEOSForklaring;
    public String mottarYtelserFraUtlandetForklaring;

    public String getArbeiderIUtlandetEllerKontinentalsokkelForklaring() {
        return arbeiderIUtlandetEllerKontinentalsokkelForklaring;
    }

    public String getMottarKontantstotteFraAnnetEOSForklaring() {
        return mottarKontantstotteFraAnnetEOSForklaring;
    }

    public String getMottarYtelserFraUtlandetForklaring() {
        return mottarYtelserFraUtlandetForklaring;
    }
}
