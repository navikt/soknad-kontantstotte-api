package no.nav.kontantstotte.oppsummering.bolk;

public class Oppsummering {
    public String bekreftelse;

    public boolean erGyldig() {
        return "JA".equalsIgnoreCase(this.bekreftelse);
    }
}
