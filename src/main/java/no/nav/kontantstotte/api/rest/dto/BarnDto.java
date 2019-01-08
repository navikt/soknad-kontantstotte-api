package no.nav.kontantstotte.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BarnDto {

    @JsonProperty
    private final String fulltnavn;

    @JsonProperty
    private final String fodselsdato;

    @JsonProperty
    private final boolean erFlerling;

    @JsonCreator
    public BarnDto(
            @JsonProperty("fulltnavn") String fulltnavn,
            @JsonProperty("fodselsdato") String fodselsdato,
            @JsonProperty("eFlerling") boolean erFlerling) {

        this.fulltnavn = fulltnavn;
        this.fodselsdato = fodselsdato;
        this.erFlerling = erFlerling;
    }

    public String getFulltnavn() {
        return fulltnavn;
    }

    public String getFodselsdato() {
        return fodselsdato;
    }

    public boolean getErFlerling() {
        return erFlerling;
    }

}
