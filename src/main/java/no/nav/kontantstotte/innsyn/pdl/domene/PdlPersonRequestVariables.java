package no.nav.kontantstotte.innsyn.pdl.domene;

public class PdlPersonRequestVariables {

    private String ident;

    PdlPersonRequestVariables() {
        // for Jackson mapping
    }

    public PdlPersonRequestVariables(String ident) {
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }
}
