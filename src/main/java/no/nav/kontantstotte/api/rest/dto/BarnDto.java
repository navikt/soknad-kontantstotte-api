package no.nav.kontantstotte.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BarnDto {

    @JsonProperty
    private final String fodselsnummer;

    @JsonProperty
    private final String fulltnavn;

    @JsonProperty
    private final String fodselsdato;

    @JsonCreator
    public BarnDto(
            @JsonProperty("fodselsnummer") String fodselsnummer,
            @JsonProperty("fulltnavn") String fulltnavn,
            @JsonProperty("fodelsdato") String fodselsdato) {

        this.fodselsnummer = fodselsnummer;
        this.fulltnavn = fulltnavn;
        this.fodselsdato = fodselsdato;
    }

    public String getFodselsnummer() {
        return fodselsnummer;
    }

    public String getFulltnavn() {
        return fulltnavn;
    }

    public String getFodselsdato() {
        return fodselsdato;
    }

}
