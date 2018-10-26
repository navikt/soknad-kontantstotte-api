package no.nav.kontantstotte.innsending.oppsummering.html;

import no.nav.kontantstotte.innsending.steg.Person;

public class MetaData {
    public String tittel;
    public MetaDataElement innsendingsTidspunkt;
    public Person person;
    public MetaDataElement bekreftelse;

    MetaData() {
    }

    MetaData(String tittel, MetaDataElement innsendingsTidspunkt, Person person, MetaDataElement bekreftelse) {
        this.tittel = tittel;
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
}

