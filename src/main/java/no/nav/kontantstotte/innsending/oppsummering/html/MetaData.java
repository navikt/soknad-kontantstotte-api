package no.nav.kontantstotte.innsending.oppsummering.html;

import no.nav.kontantstotte.innsending.steg.Person;

public class MetaData {
    public String tittel;
    public String skjemanummer;
    public MetaDataElement fastsattdato;
    public MetaDataElement innsendingsTidspunkt;
    public Person person;
    public MetaDataElement bekreftelse;

    MetaData() {
    }

    MetaData(String tittel, MetaDataElement innsendingsTidspunkt, Person person, MetaDataElement bekreftelse, String skjemanummer, MetaDataElement fastsattdato) {
        this.tittel = tittel;
        this.skjemanummer = skjemanummer;
        this.fastsattdato = fastsattdato;
        this.innsendingsTidspunkt = innsendingsTidspunkt;
        this.person = person;
        this.bekreftelse = bekreftelse;
    }

    public String getTittel() {
        return tittel;
    }

    public MetaDataElement getInnsendingsTidspunkt() {
        return innsendingsTidspunkt;
    }

    public Person getPerson() {
        return person;
    }

    public MetaDataElement getBekreftelse() {
        return bekreftelse;
    }

    public String getSkjemanummer() {
        return skjemanummer;
    }

    public MetaDataElement getFastsattdato() {
        return fastsattdato;
    }
}

