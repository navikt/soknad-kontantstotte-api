package no.nav.kontantstotte.innsending.steg;

public class Oppsummering {
    public String bekreftelse;

    public boolean harBekreftetOpplysninger() {
        return "JA".equalsIgnoreCase(this.bekreftelse);
    }
}
