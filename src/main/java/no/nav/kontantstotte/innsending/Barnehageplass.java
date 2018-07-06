package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Optional;

public class Barnehageplass {
    public String harBarnehageplass;
    public String kommune;
    public String antallTimer;
    public String fraDato;

    public Optional<String> getKommune() {
        return Optional.ofNullable(kommune);
    }

    public Optional<String> getAntallTimer() {
        return Optional.ofNullable(antallTimer);
    }

    public Optional<String> getFraDato() {
        return Optional.ofNullable(fraDato);
    }
}
