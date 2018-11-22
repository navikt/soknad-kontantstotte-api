package no.nav.kontantstotte.innsending.steg;

public class ArbeidIUtlandet {
    public boolean arbeiderIUtlandetEllerKontinentalsokkel;
    public String arbeiderIUtlandetEllerKontinentalsokkelForklaring;
    public boolean arbeiderAnnenForelderIUtlandet;
    public String arbeiderAnnenForelderIUtlandetForklaring;

    public ArbeidIUtlandet(
            boolean arbeiderIUtlandetEllerKontinentalsokkel,
            String arbeiderIUtlandetEllerKontinentalsokkelForklaring,
            boolean arbeiderAnnenForelderIUtlandet,
            String arbeiderAnnenForelderIUtlandetForklaring) {
        this.arbeiderIUtlandetEllerKontinentalsokkel = arbeiderIUtlandetEllerKontinentalsokkel;
        this.arbeiderIUtlandetEllerKontinentalsokkelForklaring = arbeiderIUtlandetEllerKontinentalsokkelForklaring;
        this.arbeiderAnnenForelderIUtlandet = arbeiderAnnenForelderIUtlandet;
        this.arbeiderAnnenForelderIUtlandetForklaring = arbeiderAnnenForelderIUtlandetForklaring;
    }
}
