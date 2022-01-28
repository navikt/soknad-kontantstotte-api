package no.nav.kontantstotte.innsyn.pdl.domene;

public class PdlPersonRequest {

    private PdlPersonRequestVariables variables;
    private String query;

    public PdlPersonRequest(PdlPersonRequestVariables variables, String query) {
        this.variables = variables;
        this.query = query;
    }

    public PdlPersonRequestVariables getVariables() {
        return variables;
    }

    public String getQuery() {
        return query;
    }
}
