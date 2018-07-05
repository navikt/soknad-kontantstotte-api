package no.nav.kontantstotte.innsending;

import java.time.LocalDateTime;

public class Soknad {

    public LocalDateTime innsendingTimestamp;
    public SokerKrav sokerKrav;
    public Familieforhold familieforhold;
    public Barnehageplass barnehageplass;
    public Arbeidsforhold arbeidsforhold;
    public Barn barn;

    public Soknad() {
        this.sokerKrav = new SokerKrav();
        this.familieforhold = new Familieforhold();
        this.barnehageplass = new Barnehageplass();
        this.arbeidsforhold = new Arbeidsforhold();
        this.barn = new Barn();
    }
}
