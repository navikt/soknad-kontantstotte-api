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

    @JsonProperty
    private boolean journalforSelv;

    public SoknadDto(@JsonProperty("fnr") String fnr, @JsonProperty("soknad") String soknad,
                     @JsonProperty("vedlegg") List<VedleggDto> vedlegg, @JsonProperty("journalforSelv") boolean journalforSelv) {
        this.fnr = fnr;
        this.soknad = soknad;
        this.vedlegg = vedlegg;
        this.journalforSelv = journalforSelv;
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

    public boolean erJournalforSelv() {
        return this.journalforSelv;
    }
}
