package no.nav.kontantstotte.innsending.steg;

public class Oppsummering {
    public boolean bekreftelse;

    public Oppsummering(boolean bekreftelse) {
        this.bekreftelse = bekreftelse;
    }

    public boolean erGyldig() {
        return bekreftelse;
    }
}
