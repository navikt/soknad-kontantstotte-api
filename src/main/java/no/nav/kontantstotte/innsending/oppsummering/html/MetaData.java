package no.nav.kontantstotte.innsending.oppsummering.html;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import no.nav.kontantstotte.innsending.steg.Person;

@JsonDeserialize(builder = MetaData.Builder.class)
public class MetaData {
    private final String tittel;
    private final String skjemanummer;
    private final MetaDataElement fastsattdato;
    private final MetaDataElement endretDato;
    private final MetaDataElement innsendingsTidspunkt;
    private final Person person;
    private final MetaDataElement bekreftelse;

    MetaData(Builder builder) {
        this.tittel = builder.tittel;
        this.skjemanummer = builder.skjemanummer;
        this.fastsattdato = builder.fastsattdato;
        this.endretDato = builder.endretDato;
        this.innsendingsTidspunkt = builder.innsendingsTidspunkt;
        this.person = builder.person;
        this.bekreftelse = builder.bekreftelse;
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

    public MetaDataElement getEndretDato() {
        return endretDato;
    }

    @JsonPOJOBuilder
    public static class Builder {
        public String tittel;
        public String skjemanummer;
        public MetaDataElement fastsattdato;
        public MetaDataElement endretDato;
        public MetaDataElement innsendingsTidspunkt;
        public Person person;
        public MetaDataElement bekreftelse;

        public Builder tittel(String tittel) {
            this.tittel = tittel;
            return this;
        }
        public Builder skjemanummer(String skjemanummer) {
            this.skjemanummer = skjemanummer;
            return this;
        }
        public Builder fastsattdato(MetaDataElement fastsattdato) {
            this.fastsattdato = fastsattdato;
            return this;
        }
        public Builder endretDato(MetaDataElement endretDato) {
            this.endretDato = endretDato;
            return this;
        }
        public Builder innsendingsTidspunkt(MetaDataElement innsendingsTidspunkt) {
            this.innsendingsTidspunkt = innsendingsTidspunkt;
            return this;
        }
        public Builder person(Person person) {
            this.person = person;
            return this;
        }
        public Builder bekreftelse(MetaDataElement bekreftelse) {
            this.bekreftelse = bekreftelse;
            return this;
        }
        public MetaData build() {
            return new MetaData(this);
        }
    }

}

