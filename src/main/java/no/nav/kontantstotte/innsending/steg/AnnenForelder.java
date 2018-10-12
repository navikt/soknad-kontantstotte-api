package no.nav.kontantstotte.innsending.steg;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnenForelder {
    @JsonProperty("annenForelderNavn")
    public String navn;
    @JsonProperty("annenForelderPersonnummer")
    public String personnummer;
    @JsonProperty("annenForelderYrkesaktivINorgeEOSIMinstFemAar")
    public String yrkesaktivINorgeEOSIMinstFemAar;
}
