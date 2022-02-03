package no.nav.kontantstotte.innsyn.pdl.domene;

import java.util.List;

public class PdlHentPersoner {

    private List<PdlHentPersonBolk> hentPersonBolk;

    PdlHentPersoner() {
        // for Jackson mapping
    }

    public PdlHentPersoner(List<PdlHentPersonBolk> hentPersonBolk) {
        this.hentPersonBolk = hentPersonBolk;
    }

    public List<PdlHentPersonBolk> getHentPersonBolk() {
        return hentPersonBolk;
    }
}
