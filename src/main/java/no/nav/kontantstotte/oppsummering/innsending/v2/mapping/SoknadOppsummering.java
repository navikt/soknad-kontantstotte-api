package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;


import no.nav.kontantstotte.oppsummering.Soknad;

import java.util.List;
import java.util.Map;

public class SoknadOppsummering {
    private final Soknad soknad;
    private final List<Bolk> bolker;
    private final Map<String, String> tekster;

    public SoknadOppsummering(Soknad soknad, List<Bolk> bolker, Map<String, String> tekster) {
        this.soknad = soknad;
        this.bolker = bolker;
        this.tekster = tekster;
    }
}
