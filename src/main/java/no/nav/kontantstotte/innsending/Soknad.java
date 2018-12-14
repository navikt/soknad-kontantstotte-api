package no.nav.kontantstotte.innsending;

import no.nav.kontantstotte.innsending.steg.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

public class Soknad {

    public String soknadsId;
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
    public List<VedleggMetadata> vedlegg;

    public Soknad() {
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
        this.vedlegg = new ArrayList<>();
    }

    public boolean erGyldig() {
        return this.oppsummering.erGyldig();
    }

    public void markerInnsendingsTidspunkt() {
        innsendingsTidspunkt = now();
    }
}
