package no.nav.kontantstotte.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BarnDto {

    @JsonProperty
    private final String fulltnavn;

    @JsonProperty
    private final String fødselsdato;

    @JsonCreator
    public BarnDto(
            @JsonProperty("fulltnavn") String fulltnavn,
            @JsonProperty("fødselsdato") String fødselsdato) {

        this.fulltnavn = fulltnavn;
        this.fødselsdato = fødselsdato;
    }

    public String getFulltnavn() {
        return fulltnavn;
    }

    public String getFødselsdato() {
        return fødselsdato;
    }

}
