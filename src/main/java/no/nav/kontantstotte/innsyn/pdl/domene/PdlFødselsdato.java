package no.nav.kontantstotte.innsyn.pdl.domene;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PdlFødselsdato {

    @JsonProperty("foedselsdato")
    private String fødselsdato;

    public PdlFødselsdato(String fødselsdato) {
        this.fødselsdato = fødselsdato;
    }

    public String getFødselsdato() {
        return fødselsdato;
    }
}
