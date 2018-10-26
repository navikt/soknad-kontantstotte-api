package no.nav.kontantstotte.innsending.oppsummering.html;

public class MetaDataElement {
    public String tekst;
    public String verdi;

    public MetaDataElement() {
    }

    public MetaDataElement(String tekst, String verdi) {
        this.tekst = tekst;
        this.verdi = verdi;
    }

    public String getTekst() {
        return tekst;
    }

    public String getVerdi() {
        return verdi;
    }
}
