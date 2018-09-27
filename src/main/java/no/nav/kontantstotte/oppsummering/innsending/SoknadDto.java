package no.nav.kontantstotte.oppsummering.innsending;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

class SoknadDto {

    @JsonProperty
    private final byte[] pdf;

    @JsonProperty
    private final String fnr;

    @JsonProperty
    private final LocalDateTime innsendingTimestamp;

    SoknadDto(String fnr, byte[] pdf, LocalDateTime innsendingTimestamp) {
        this.fnr = fnr;
        this.pdf = pdf;
        this.innsendingTimestamp = innsendingTimestamp;
    }

    byte[] getPdf() {
        return pdf;
    }

    String getFnr() {
        return fnr;
    }
}

