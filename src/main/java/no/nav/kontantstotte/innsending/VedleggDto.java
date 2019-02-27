package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.annotation.JsonProperty;

class VedleggDto {

    @JsonProperty
    private final byte[] data;

    @JsonProperty
    private final String tittel;

    VedleggDto(
            byte[] data,
            String tittel
    ) {
        this.data = data;
        this.tittel = tittel;
    }

    byte[] getData() {
        return data;
    }

    String getTittel() {
        return tittel;
    }

}
