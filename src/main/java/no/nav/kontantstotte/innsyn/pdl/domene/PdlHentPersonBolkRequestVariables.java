package no.nav.kontantstotte.innsyn.pdl.domene;

import java.util.List;

public class PdlHentPersonBolkRequestVariables {

    private List<String> identer;

    PdlHentPersonBolkRequestVariables() {
        // for Jackson mapping
    }

    public PdlHentPersonBolkRequestVariables(List<String> identer) {
        this.identer = identer;
    }

    public List<String> getIdenter() {
        return identer;
    }
}
