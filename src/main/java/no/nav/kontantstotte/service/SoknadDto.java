package no.nav.kontantstotte.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SoknadDto {
    @JsonProperty
    private final byte[] pdf;

    @JsonProperty
    private final String fnr;

    public SoknadDto(String fnr, byte[] pdf) {
        this.fnr = fnr;
        this.pdf = pdf;
    }

    byte[] getPdf() {
        return pdf;
    }

    String getFnr() {
        return fnr;
    }
}

