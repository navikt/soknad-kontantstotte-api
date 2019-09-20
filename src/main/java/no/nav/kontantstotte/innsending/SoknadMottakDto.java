package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.List;

public class SoknadMottakDto {

    @JsonProperty
    private final String fnr;

    @JsonProperty
    private final Soknad soknad;

    @JsonProperty
    private final List<VedleggDto> vedlegg;

    public SoknadMottakDto(String fnr, Soknad soknad, List<VedleggDto> vedlegg) {
        this.fnr = fnr;
        this.soknad = soknad;
        this.vedlegg = vedlegg;
    }

    public String getFnr() {
        return this.fnr;
    }

    public Soknad getSoknad() {
        return this.soknad;
    }

    public List<VedleggDto> getVedlegg() {
        return this.vedlegg;
    }
}
