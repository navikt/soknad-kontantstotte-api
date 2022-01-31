package no.nav.kontantstotte.innsyn.pdl.domene;

public class PdlPersonRequestVariables<T> {

    private T ident;

    PdlPersonRequestVariables() {
        // for Jackson mapping
    }

    public PdlPersonRequestVariables(T ident) {
        this.ident = ident;
    }

    public T getIdent() {
        return ident;
    }
}
