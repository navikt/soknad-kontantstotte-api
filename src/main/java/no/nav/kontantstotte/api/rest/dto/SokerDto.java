package no.nav.kontantstotte.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SokerDto {

    @JsonProperty
    private final String innloggetSom;

    public SokerDto(@JsonProperty("innloggetSom") String innloggetSom) {
        this.innloggetSom = innloggetSom;
    }

    public String getInnloggetSom() {
        return innloggetSom;
    }
}
