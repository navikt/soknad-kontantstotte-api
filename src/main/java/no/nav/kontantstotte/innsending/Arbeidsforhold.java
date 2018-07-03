package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Optional;

public class Arbeidsforhold {

    public String arbeiderIUtlandetEllerKontinentalsokkel;
    public String mottarKontantstotteFraAnnetEOS;
    public String mottarYtelserFraUtlandet;
    @JsonAlias({"mottarYtelserFraUtlandetForklaring",
            "arbeiderIUtlandetEllerKontinentalsokkelForklaring",
            "mottarKontantstotteFraAnnetEOSForklaring"})
    public Optional<String> forklaring;
}
