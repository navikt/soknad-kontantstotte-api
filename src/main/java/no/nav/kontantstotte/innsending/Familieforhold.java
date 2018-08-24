package no.nav.kontantstotte.innsending;


import java.util.Optional;

public class Familieforhold {
    public String borForeldreneSammenMedBarnet;
    public String erAvklartDeltBosted;
    //public AnnenForelder annenForelder;
    public String annenForelderNavn;
    public String annenForelderFodselsnummer;
    public String annenForelderYrkesaktivINorgeEOSIMinstFemAar;

    public Optional<String> getAnnenForelderNavn() {
        return Optional.ofNullable(annenForelderNavn);
    }
    public Optional<String> getAnnenForelderFodselsnummer() {
        return Optional.ofNullable(annenForelderNavn);
    }
    public Optional<String> getAnnenForelderYrkesaktivINorgeEOSMinstFemAar() {
        return Optional.ofNullable(annenForelderYrkesaktivINorgeEOSIMinstFemAar);
    }

}
