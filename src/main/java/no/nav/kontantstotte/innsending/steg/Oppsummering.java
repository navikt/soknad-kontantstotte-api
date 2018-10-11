package no.nav.kontantstotte.innsending.steg;

public class Oppsummering {
    public String bekreftelse;

    public boolean erGyldig() {
        return "JA".equalsIgnoreCase(this.bekreftelse);
    }
}
