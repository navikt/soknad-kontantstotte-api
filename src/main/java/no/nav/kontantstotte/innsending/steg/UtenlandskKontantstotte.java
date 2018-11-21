package no.nav.kontantstotte.innsending.steg;

public class UtenlandskKontantstotte {
    public String mottarKontantstotteFraUtlandet; 
    public String mottarKontantstotteFraUtlandetTilleggsinfo;

    public UtenlandskKontantstotte(
            String mottarKontantstotteFraUtlandet,
            String mottarKontantstotteFraUtlandetTilleggsinfo) {
        this.mottarKontantstotteFraUtlandet = mottarKontantstotteFraUtlandet;
        this.mottarKontantstotteFraUtlandetTilleggsinfo = mottarKontantstotteFraUtlandetTilleggsinfo;
    }
}
