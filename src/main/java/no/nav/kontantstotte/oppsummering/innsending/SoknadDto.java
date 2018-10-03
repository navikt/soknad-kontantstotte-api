package no.nav.kontantstotte.oppsummering.innsending;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDateTime;

class SoknadDto {

    @JsonProperty
    private final byte[] pdf;

    @JsonProperty
    private final String fnr;

    @JsonProperty
    private final Instant innsendingTimestamp;

    SoknadDto(String fnr, byte[] pdf, Instant innsendingTimestamp) {
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

