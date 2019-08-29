package no.nav.kontantstotte.innsending;

import no.nav.kontantstotte.innsending.steg.*;

import java.time.Instant;

import static java.time.Instant.now;

public class Soknad {

    public Veiledning veiledning;
    public Instant innsendingsTidspunkt;
    public Person person;
    public SokerKrav kravTilSoker;
    public Familieforhold familieforhold;
    public Barnehageplass barnehageplass;
    public ArbeidIUtlandet arbeidIUtlandet;
    public UtenlandskKontantstotte utenlandskKontantstotte;
    public Barn mineBarn;
    public TilknytningTilUtland tilknytningTilUtland;
    public UtenlandskeYtelser utenlandskeYtelser;
    public Oppsummering oppsummering;
    public String sprak;

    public Soknad() {
        this.veiledning = new Veiledning();
        this.person = new Person();
        this.kravTilSoker = new SokerKrav();
        this.familieforhold = new Familieforhold();
        this.barnehageplass = new Barnehageplass();
        this.arbeidIUtlandet = new ArbeidIUtlandet();
        this.utenlandskeYtelser = new UtenlandskeYtelser();
        this.oppsummering = new Oppsummering();
        this.utenlandskKontantstotte = new UtenlandskKontantstotte();
        this.mineBarn = new Barn();
        this.tilknytningTilUtland = new TilknytningTilUtland();
    }

    public boolean erGyldig() {
        return this.veiledning.erGyldig() && this.oppsummering.erGyldig();
    }

    public void markerInnsendingsTidspunkt() {
        innsendingsTidspunkt = now();
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
