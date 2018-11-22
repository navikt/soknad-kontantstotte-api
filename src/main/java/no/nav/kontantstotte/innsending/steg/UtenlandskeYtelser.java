package no.nav.kontantstotte.innsending.steg;

public class UtenlandskeYtelser {
    public boolean mottarYtelserFraUtland;
    public String mottarYtelserFraUtlandForklaring;
    public boolean mottarAnnenForelderYtelserFraUtland;
    public String mottarAnnenForelderYtelserFraUtlandForklaring;

    public UtenlandskeYtelser(
            boolean mottarYtelserFraUtland,
            String mottarYtelserFraUtlandForklaring,
            boolean mottarAnnenForelderYtelserFraUtland,
            String mottarAnnenForelderYtelserFraUtlandForklaring) {
        this.mottarYtelserFraUtland = mottarYtelserFraUtland;
        this.mottarYtelserFraUtlandForklaring = mottarYtelserFraUtlandForklaring;
        this.mottarAnnenForelderYtelserFraUtland = mottarAnnenForelderYtelserFraUtland;
        this.mottarAnnenForelderYtelserFraUtlandForklaring = mottarAnnenForelderYtelserFraUtlandForklaring;
    }
}
