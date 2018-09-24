package no.nav.kontantstotte.oppsummering.innsending;

import com.fasterxml.jackson.annotation.JsonProperty;

class SoknadDto {
    @JsonProperty
    private final byte[] pdf;

    @JsonProperty
    private final String fnr;

    SoknadDto(String fnr, byte[] pdf) {
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

