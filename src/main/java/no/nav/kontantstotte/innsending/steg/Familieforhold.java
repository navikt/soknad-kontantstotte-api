package no.nav.kontantstotte.innsending.steg;

public class Familieforhold {
    public boolean borForeldreneSammenMedBarnet;
    public String annenForelderNavn;
    public String annenForelderFodselsnummer;

    public Familieforhold(
            boolean borForeldreneSammenMedBarnet,
            String annenForelderNavn,
            String annenForelderFodselsnummer) {
        this.borForeldreneSammenMedBarnet = borForeldreneSammenMedBarnet;
        this.annenForelderNavn = annenForelderNavn;
        this.annenForelderFodselsnummer = annenForelderFodselsnummer;
    }
}
