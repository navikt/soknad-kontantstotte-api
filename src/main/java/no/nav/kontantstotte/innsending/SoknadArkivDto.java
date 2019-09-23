package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

class SoknadArkivDto {

    @JsonProperty
    private final byte[] pdf;

    @JsonProperty
    private final String fnr;

    @JsonProperty
    private final Instant innsendingsTidspunkt;

    @JsonProperty
    private final List<VedleggDto> vedlegg;

    SoknadArkivDto(String fnr, byte[] pdf, Instant innsendingsTidspunkt, List<VedleggDto> vedlegg) {
        this.fnr = fnr;
        this.pdf = pdf;
        this.innsendingsTidspunkt = innsendingsTidspunkt;
        this.vedlegg = vedlegg;
    }

    byte[] getPdf() {
        return pdf;
    }

    String getFnr() {
        return fnr;
    }
}

