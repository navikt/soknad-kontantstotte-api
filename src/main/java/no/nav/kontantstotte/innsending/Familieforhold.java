package no.nav.kontantstotte.innsending;


import java.util.Optional;

public class Familieforhold {
    public String borForeldreneSammenMedBarnet;
    public String erAvklartDeltBosted;
    //public AnnenForelder annenForelder;
    public String annenForelderNavn;
    public String annenForelderFodselsnummer;
    public String annenForelderYrkesaktivINorgeEOSIMinstFemAar;

    public String getAnnenForelderNavn() {
        return annenForelderNavn;
    }
    public String getAnnenForelderFodselsnummer() {
        return annenForelderFodselsnummer;
    }
    public Optional<String> getAnnenForelderYrkesaktivINorgeEOSMinstFemAar() {
        return Optional.ofNullable(annenForelderYrkesaktivINorgeEOSIMinstFemAar);
    }

}
