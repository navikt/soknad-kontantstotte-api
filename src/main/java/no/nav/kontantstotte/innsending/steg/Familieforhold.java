package no.nav.kontantstotte.innsending.steg;

public class Familieforhold {
    public String borForeldreneSammenMedBarnet;
    public String annenForelderNavn;
    public String annenForelderFodselsnummer;

    public Familieforhold(
            String borForeldreneSammenMedBarnet,
            String annenForelderNavn,
            String annenForelderFodselsnummer) {
        this.borForeldreneSammenMedBarnet = borForeldreneSammenMedBarnet;
        this.annenForelderNavn = annenForelderNavn;
        this.annenForelderFodselsnummer = annenForelderFodselsnummer;
    }
}
