package no.nav.kontantstotte.innsending.steg;

public class ArbeidIUtlandet {
    public String arbeiderIUtlandetEllerKontinentalsokkel;
    public String arbeiderIUtlandetEllerKontinentalsokkelForklaring;
    public String arbeiderAnnenForelderIUtlandet;
    public String arbeiderAnnenForelderIUtlandetForklaring;

    public ArbeidIUtlandet(
            String arbeiderIUtlandetEllerKontinentalsokkel,
            String arbeiderIUtlandetEllerKontinentalsokkelForklaring,
            String arbeiderAnnenForelderIUtlandet,
            String arbeiderAnnenForelderIUtlandetForklaring) {
        this.arbeiderIUtlandetEllerKontinentalsokkel = arbeiderIUtlandetEllerKontinentalsokkel;
        this.arbeiderIUtlandetEllerKontinentalsokkelForklaring = arbeiderIUtlandetEllerKontinentalsokkelForklaring;
        this.arbeiderAnnenForelderIUtlandet = arbeiderAnnenForelderIUtlandet;
        this.arbeiderAnnenForelderIUtlandetForklaring = arbeiderAnnenForelderIUtlandetForklaring;
    }
}
