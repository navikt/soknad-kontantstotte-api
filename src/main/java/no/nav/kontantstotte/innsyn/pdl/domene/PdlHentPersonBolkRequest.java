package no.nav.kontantstotte.innsyn.pdl.domene;

public class PdlHentPersonBolkRequest {

    private PdlHentPersonBolkRequestVariables variables;
    private String query;

    PdlHentPersonBolkRequest() {
        // for Jackson mapping
    }

    public PdlHentPersonBolkRequest(PdlHentPersonBolkRequestVariables variables, String query) {
        this.variables = variables;
        this.query = query;
    }

    public PdlHentPersonBolkRequestVariables getVariables() {
        return variables;
    }

    public String getQuery() {
        return query;
    }
}
