package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SoknadDto {

    @JsonProperty
    private final String fnr;

    @JsonProperty
    private final String soknad;

    @JsonProperty
    private final List<VedleggDto> vedlegg;

    public SoknadDto(@JsonProperty("fnr") String fnr, @JsonProperty("soknad") String soknad, @JsonProperty("vedlegg") List<VedleggDto> vedlegg) {
        this.fnr = fnr;
        this.soknad = soknad;
        this.vedlegg = vedlegg;
    }

    public String getFnr() {
        return this.fnr;
    }

    public String getSoknad() {
        return this.soknad;
    }

    public List<VedleggDto> getVedlegg() {
        return this.vedlegg;
    }
}
