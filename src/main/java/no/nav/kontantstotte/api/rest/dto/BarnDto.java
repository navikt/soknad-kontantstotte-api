package no.nav.kontantstotte.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BarnDto {

    @JsonProperty
    private final String fulltnavn;

    @JsonProperty
    private final String fødselsdato;

    @JsonProperty
    private final String fødselsnummer;

    @JsonCreator
    public BarnDto(
            @JsonProperty("fulltnavn") String fulltnavn,
            @JsonProperty("fødselsdato") String fødselsdato,
            @JsonProperty("fødselsnummer") String fødselsnummer) {

        this.fulltnavn = fulltnavn;
        this.fødselsdato = fødselsdato;
        this.fødselsnummer = fødselsnummer;
    }

    public String getFulltnavn() {
        return fulltnavn;
    }

    public String getFødselsdato() {
        return fødselsdato;
    }

    public String getFødselsnummer() {
        return fødselsnummer;
    }

}
