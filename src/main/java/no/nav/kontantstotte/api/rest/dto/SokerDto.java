package no.nav.kontantstotte.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SokerDto {

    @JsonProperty
    private final String innloggetSom;

    @JsonProperty
    private final String fornavn;

    @JsonProperty
    private final String fulltnavn;

    @JsonProperty
    private final String statsborgerskap;

    @JsonCreator
    public SokerDto(
            @JsonProperty("innloggetSom") String innloggetSom,
            @JsonProperty("fornavn") String fornavn,
            @JsonProperty("fulltnavn") String fulltnavn,
            @JsonProperty("statsborgerskap") String statsborgerskap) {

        this.innloggetSom = innloggetSom;
        this.fornavn = fornavn;
        this.fulltnavn = fulltnavn;
        this.statsborgerskap = statsborgerskap;
    }

    public String getInnloggetSom() {
        return innloggetSom;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getFulltnavn() {
        return fulltnavn;
    }

    public String getStatsborgerskap() {
        return statsborgerskap;
    }
}
