package no.nav.kontantstotte.innsending.steg;

public class Veiledning {
    public String bekreftelse;

    public boolean harBekreftetPlikter() {
        return "JA".equalsIgnoreCase(this.bekreftelse);
    }
}
