package no.nav.kontantstotte.innsyn.pdl.domene;

public class PdlPerson {

    private PdlPersonData person;

    public PdlPerson(PdlPersonData person) {
        this.person = person;
    }

    public PdlPersonData getPerson() {
        return person;
    }
}
