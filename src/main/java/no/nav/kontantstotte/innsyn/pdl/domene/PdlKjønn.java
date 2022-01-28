package no.nav.kontantstotte.innsyn.pdl.domene;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PdlKjønn {

    @JsonProperty("kjoenn")
    private Kjønn kjønn;

    public PdlKjønn(Kjønn kjønn) {
        this.kjønn = kjønn;
    }

    public Kjønn getKjønn() {
        return kjønn;
    }
}

enum Kjønn {
    MANN, KVINNE, UKJENT
}