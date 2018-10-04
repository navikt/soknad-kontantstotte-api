package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;


import no.nav.kontantstotte.oppsummering.Soknad;

import java.util.List;
import java.util.Map;

public class SoknadOppsummering {
    private final Soknad soknad;
    private final String fnr;
    private final List<Bolk> bolker;
    private final Map<String, String> tekster;

    public SoknadOppsummering(Soknad soknad, String fnr, List<Bolk> bolker, Map<String, String> tekster) {
        this.soknad = soknad;
        this.fnr = fnr;
        this.bolker = bolker;
        this.tekster = tekster;
    }

    public Soknad getSoknad() {
        return soknad;
    }

    public String getFnr() {
        return fnr;
    }

    public List<Bolk> getBolker() {
        return bolker;
    }

    public Map<String, String> getTekster() {
        return tekster;
    }
}
