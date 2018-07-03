package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Optional;

public class Barnehageplass {
    public String harBarnehageplass;
    @JsonAlias({"jaKommune", "jaSkalSlutteKommune", "neiHarFaattKommune"})
    public Optional<String> kommune;
    @JsonAlias({"jaAntallTimer", "jaSkalSlutteAntallTimer"})
    public Optional<String> antallTimer;
    @JsonAlias({"jaFraDato", "jaSkalSlutteFraDato", "neiHarFaattPlassFraDato"})
    public Optional<String> fraDato;
}
