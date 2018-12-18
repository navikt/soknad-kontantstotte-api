package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.annotation.JsonProperty;

class VedleggDto {

    @JsonProperty
    private final byte[] data;

    @JsonProperty
    private final String tittel;

    @JsonProperty
    private final String dokumenttype;

    VedleggDto(
            byte[] data,
            String tittel,
            String dokumenttype) {
        this.data = data;
        this.tittel = tittel;
        this.dokumenttype = dokumenttype;
    }

    byte[] getData() {
        return data;
    }

    String getTittel() {
        return tittel;
    }

    String getDokumenttype() {
        return dokumenttype;
    }

}
