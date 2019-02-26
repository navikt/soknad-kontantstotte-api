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
            String tittel
    ) {
        this.data = data;
        this.tittel = tittel;
        this.dokumenttype = "PDF";
    }

    byte[] getData() {
        return data;
    }

    String getTittel() {
        return tittel;
    }

}
