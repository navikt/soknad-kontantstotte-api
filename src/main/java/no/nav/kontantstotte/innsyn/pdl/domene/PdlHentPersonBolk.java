package no.nav.kontantstotte.innsyn.pdl.domene;

public class PdlHentPersonBolk {

    private String ident;
    private PdlPersonData person;

    PdlHentPersonBolk() {
        // for Jackson mapping
    }

    public PdlHentPersonBolk(String ident, PdlPersonData person) {
        this.ident = ident;
        this.person = person;
    }

    public String getIdent() {
        return ident;
    }

    public PdlPersonData getPerson() {
        return person;
    }
}
