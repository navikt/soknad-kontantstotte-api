package no.nav.kontantstotte.innsending.steg;

public class UtenlandskeYtelser {
    public String mottarYtelserFraUtland;
    public String mottarYtelserFraUtlandForklaring;
    public String mottarAnnenForelderYtelserFraUtland;
    public String mottarAnnenForelderYtelserFraUtlandForklaring;

    public UtenlandskeYtelser(
            String mottarYtelserFraUtland,
            String mottarYtelserFraUtlandForklaring,
            String mottarAnnenForelderYtelserFraUtland,
            String mottarAnnenForelderYtelserFraUtlandForklaring) {
        this.mottarYtelserFraUtland = mottarYtelserFraUtland;
        this.mottarYtelserFraUtlandForklaring = mottarYtelserFraUtlandForklaring;
        this.mottarAnnenForelderYtelserFraUtland = mottarAnnenForelderYtelserFraUtland;
        this.mottarAnnenForelderYtelserFraUtlandForklaring = mottarAnnenForelderYtelserFraUtlandForklaring;
    }
}
