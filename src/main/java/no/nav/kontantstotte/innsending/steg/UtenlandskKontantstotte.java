package no.nav.kontantstotte.innsending.steg;

public class UtenlandskKontantstotte {
    public boolean mottarKontantstotteFraUtlandet;
    public String mottarKontantstotteFraUtlandetTilleggsinfo;

    public UtenlandskKontantstotte(
            boolean mottarKontantstotteFraUtlandet,
            String mottarKontantstotteFraUtlandetTilleggsinfo) {
        this.mottarKontantstotteFraUtlandet = mottarKontantstotteFraUtlandet;
        this.mottarKontantstotteFraUtlandetTilleggsinfo = mottarKontantstotteFraUtlandetTilleggsinfo;
    }
}
