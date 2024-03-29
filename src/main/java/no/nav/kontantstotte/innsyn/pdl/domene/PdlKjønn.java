package no.nav.kontantstotte.innsyn.pdl.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PdlKjønn {

    @JsonProperty("kjoenn")
    private Kjønn kjønn;

    PdlKjønn() {
        // for Jackson mapping
    }

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