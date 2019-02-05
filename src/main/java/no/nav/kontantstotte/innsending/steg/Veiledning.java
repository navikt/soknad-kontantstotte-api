package no.nav.kontantstotte.innsending.steg;

public class Veiledning {
    public String bekreftelse;

    public boolean erGyldig() {
        return "JA".equalsIgnoreCase(this.bekreftelse);
    }
}
