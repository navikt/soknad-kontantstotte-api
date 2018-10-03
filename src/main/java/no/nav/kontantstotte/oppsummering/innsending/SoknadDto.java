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
    private final Instant innsendingsTidspunkt;

    SoknadDto(String fnr, byte[] pdf, Instant innsendingsTidspunkt) {
        this.fnr = fnr;
        this.pdf = pdf;
        this.innsendingsTidspunkt = innsendingsTidspunkt;
    }

    byte[] getPdf() {
        return pdf;
    }

    String getFnr() {
        return fnr;
    }
}

