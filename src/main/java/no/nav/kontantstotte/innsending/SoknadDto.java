package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SoknadDto {

    @JsonProperty
    private final String soknad;

    @JsonProperty
    private final List<VedleggDto> vedlegg;

    public SoknadDto(@JsonProperty("soknad") String soknad, @JsonProperty("vedlegg") List<VedleggDto> vedlegg) {
        this.soknad = soknad;
        this.vedlegg = vedlegg;
    }

    public String getSoknad() {
        return this.soknad;
    }

    public List<VedleggDto> getVedlegg() {
        return this.vedlegg;
    }
}
