package no.nav.kontantstotte.innsending;

import no.nav.kontantstotte.innsending.steg.*;

import java.time.Instant;

import static java.time.Instant.now;

public class Soknad {

    private Instant innsendingsTidspunkt;
    private Person person;
    private SokerKrav kravTilSoker;
    private Familieforhold familieforhold;
    private Barnehageplass barnehageplass;
    private ArbeidIUtlandet arbeidIUtlandet;
    private UtenlandskKontantstotte utenlandskKontantstotte;
    private Barn mineBarn;
    private TilknytningTilUtland tilknytningTilUtland;
    private UtenlandskeYtelser utenlandskeYtelser;
    private Oppsummering oppsummering;
    private String sprak;

    public Soknad(Builder builder) {
        this.person = builder.person;
        this.kravTilSoker = builder.kravTilSoker;
        this.familieforhold = builder.familieforhold;
        this.barnehageplass = builder.barnehageplass;
        this.arbeidIUtlandet = builder.arbeidIUtlandet;
        this.utenlandskeYtelser = builder.utenlandskeYtelser;
        this.oppsummering = builder.oppsummering;
        this.utenlandskKontantstotte = builder.utenlandskKontantstotte;
        this.mineBarn = builder.mineBarn;
        this.tilknytningTilUtland = builder.tilknytningTilUtland;
        this.sprak = builder.sprak;
    }

    public Person getPerson() {
        return person;
    }

    public SokerKrav getKravTilSoker() {
        return kravTilSoker;
    }

    public Familieforhold getFamilieforhold() {
        return familieforhold;
    }

    public Barnehageplass getBarnehageplass() {
        return barnehageplass;
    }

    public ArbeidIUtlandet getArbeidIUtlandet() {
        return arbeidIUtlandet;
    }

    public UtenlandskKontantstotte getUtenlandskKontantstotte() {
        return utenlandskKontantstotte;
    }

    public Barn getMineBarn() {
        return mineBarn;
    }

    public TilknytningTilUtland getTilknytningTilUtland() {
        return tilknytningTilUtland;
    }

    public UtenlandskeYtelser getUtenlandskeYtelser() {
        return utenlandskeYtelser;
    }

    public Oppsummering getOppsummering() {
        return oppsummering;
    }

    public String getSprak() {
        return sprak;
    }

    public void setInnsendingsTidspunkt(Instant innsendingsTidspunkt) {
        this.innsendingsTidspunkt = innsendingsTidspunkt;
    }

    public boolean erGyldig() {
        return this.oppsummering != null && this.oppsummering.erGyldig();
    }

    public void markerInnsendingsTidspunkt() {
        innsendingsTidspunkt = now();
    }

    public Instant getInnsendingsTidspunkt() {
        return innsendingsTidspunkt;
    }

    public static class Builder {

        private Person person;
        private SokerKrav kravTilSoker;
        private Familieforhold familieforhold;
        private Barnehageplass barnehageplass;
        private ArbeidIUtlandet arbeidIUtlandet;
        private UtenlandskKontantstotte utenlandskKontantstotte;
        private Barn mineBarn;
        private TilknytningTilUtland tilknytningTilUtland;
        private UtenlandskeYtelser utenlandskeYtelser;
        private Oppsummering oppsummering;
        private String sprak;

        public Builder person(Person person) {
            this.person = person;
            return this;
        }

        public Builder kravTilSoker(SokerKrav kravTilSoker) {
            this.kravTilSoker = kravTilSoker;
            return this;
        }

        public Builder familieforhold(Familieforhold familieforhold) {
            this.familieforhold = familieforhold;
            return this;
        }

        public Builder barnehageplass(Barnehageplass barnehageplass) {
            this.barnehageplass = barnehageplass;
            return this;
        }

        public Builder arbeidIUtlandet(ArbeidIUtlandet arbeidIUtlandet) {
            this.arbeidIUtlandet = arbeidIUtlandet;
            return this;
        }

        public Builder utenlandskKontantstotte(UtenlandskKontantstotte utenlandskKontantstotte) {
            this.utenlandskKontantstotte = utenlandskKontantstotte;
            return this;
        }

        public Builder mineBarn(Barn mineBarn) {
            this.mineBarn = mineBarn;
            return this;
        }

        public Builder tilknytningTilUtland(TilknytningTilUtland tilknytningTilUtland) {
            this.tilknytningTilUtland = tilknytningTilUtland;
            return this;
        }

        public Builder utenlandskeYtelser(UtenlandskeYtelser utenlandskeYtelser) {
            this.utenlandskeYtelser = utenlandskeYtelser;
            return this;
        }

        public Builder oppsummering(Oppsummering oppsummering) {
            this.oppsummering = oppsummering;
            return this;
        }

        public Builder sprak(String sprak) {
            this.sprak = sprak;
            return this;
        }

        public Soknad build() {
            return new Soknad(this);
        }

    }
}
