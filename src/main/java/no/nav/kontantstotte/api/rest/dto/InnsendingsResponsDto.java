package no.nav.kontantstotte.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InnsendingsResponsDto {

    @JsonProperty
    private final String innsendtDato;

    public InnsendingsResponsDto(@JsonProperty("innsendtDato") String innsendtDato) {
        this.innsendtDato = innsendtDato;
    }

    public String getInnsendtDato() {
        return innsendtDato;
    }
}
