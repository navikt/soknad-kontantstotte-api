package no.nav.kontantstotte.innsyn.pdl.domene;

public class PdlPersonRequestVariables<T> {

    private T ident;

    public PdlPersonRequestVariables(T ident) {
        this.ident = ident;
    }

    public T getIdent() {
        return ident;
    }
}
