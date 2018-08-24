package no.nav.kontantstotte.innsending;

import java.time.LocalDateTime;

public class Soknad {

    public LocalDateTime innsendingTimestamp;
    public SokerKrav kravTilSoker;
    public Familieforhold familieforhold;
    public Barnehageplass barnehageplass;
    public Arbeidsforhold arbeidsforhold;
    public Barn mineBarn;

    public Soknad() {
        this.kravTilSoker = new SokerKrav();
        this.familieforhold = new Familieforhold();
        this.barnehageplass = new Barnehageplass();
        this.arbeidsforhold = new Arbeidsforhold();
        this.mineBarn = new Barn();
    }
}
