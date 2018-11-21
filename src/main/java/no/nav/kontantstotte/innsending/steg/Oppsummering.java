package no.nav.kontantstotte.innsending.steg;

public class Oppsummering {
    public String bekreftelse;

    public Oppsummering(String bekreftelse) {
        this.bekreftelse = bekreftelse;
    }

    public boolean erGyldig() {
        return "JA".equalsIgnoreCase(this.bekreftelse);
    }
}
