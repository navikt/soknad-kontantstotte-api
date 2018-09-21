package no.nav.kontantstotte.oppsummering;

import java.time.LocalDateTime;

public class Soknad {

    public LocalDateTime innsendingTimestamp;
    public Person person;
    public SokerKrav kravTilSoker;
    public Familieforhold familieforhold;
    public Barnehageplass barnehageplass;
    public Arbeidsforhold arbeidsforhold;
    public UtenlandskKontantstotte utenlandskKontantstotte;
    public Barn mineBarn;
    public UtenlandskeYtelser utenlandskeYtelser;
    public String sprak;

    public Soknad() {
        this.person = new Person();
        this.kravTilSoker = new SokerKrav();
        this.familieforhold = new Familieforhold();
        this.barnehageplass = new Barnehageplass();
        this.arbeidsforhold = new Arbeidsforhold();
        this.utenlandskeYtelser = new UtenlandskeYtelser();
        this.utenlandskKontantstotte = new UtenlandskKontantstotte();
        this.mineBarn = new Barn();
    }
}
