package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VedleggMetadata {

    private final String vedleggsId;

    private final String tittel;

    private final String dokumenttype;

    public VedleggMetadata(
            @JsonProperty("vedleggsId") String vedleggsId,
            @JsonProperty("tittel") String tittel,
            @JsonProperty("dokumenttype") String dokumenttype) {
        this.vedleggsId = vedleggsId;
        this.tittel = tittel;
        this.dokumenttype = dokumenttype;
    }

    public String getVedleggsId() {
        return vedleggsId;
    }

    public String getTittel() {
        return tittel;
    }

    public String getDokumenttype() {
        return dokumenttype;
    }
}
