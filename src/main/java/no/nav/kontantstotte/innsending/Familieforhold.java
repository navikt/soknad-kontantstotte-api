package no.nav.kontantstotte.innsending;


import java.util.Optional;

public class Familieforhold {
    public String borForeldreneSammenMedBarnet;
    public String erAvklartDeltBosted;
    public AnnenForelder annenForelder;

    public Optional<AnnenForelder> getAnnenForelder() {
        return Optional.ofNullable(annenForelder);
    }
}
