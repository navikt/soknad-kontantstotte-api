package no.nav.kontantstotte.innsyn.pdl.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PdlFødselsdato {

    @JsonProperty("foedselsdato")
    private String fødselsdato;

    PdlFødselsdato() {
        // for Jackson mapping
    }

    public PdlFødselsdato(String fødselsdato) {
        this.fødselsdato = fødselsdato;
    }

    public String getFødselsdato() {
        return fødselsdato;
    }
}
