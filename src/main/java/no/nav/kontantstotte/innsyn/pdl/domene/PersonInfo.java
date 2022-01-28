package no.nav.kontantstotte.innsyn.pdl.domene;

import java.time.LocalDate;

public class PersonInfo {

    private String ident;
    private LocalDate fødselsdato;
    private String navn;
    private Kjønn kjønn;

    PersonInfo() {
        // privat konstruktor
    }

    public String getIdent() {
        return ident;
    }

    public LocalDate getFødselsdato() {
        return fødselsdato;
    }

    public String getNavn() {
        return navn;
    }

    public Kjønn getKjønn() {
        return kjønn;
    }

    public static class Builder {

        private PersonInfo personInfo = new PersonInfo();

        public Builder ident(String ident) {
            personInfo.ident = ident;
            return this;
        }

        public Builder fødselsdato(LocalDate fødselsdato) {
            personInfo.fødselsdato = fødselsdato;
            return this;
        }

        public Builder navn(String navn) {
            personInfo.navn = navn;
            return this;
        }

        public Builder kjønn(Kjønn kjønn) {
            personInfo.kjønn = kjønn;
            return this;
        }

        public PersonInfo build() {
            return personInfo;
        }
    }

}
