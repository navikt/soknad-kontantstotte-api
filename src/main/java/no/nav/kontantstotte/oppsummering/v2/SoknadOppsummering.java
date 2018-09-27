package no.nav.kontantstotte.oppsummering.v2;

import no.nav.kontantstotte.oppsummering.v1.Soknad;

import java.util.List;

public class SoknadOppsummering {
    private final Soknad soknad;
    private final List<Bolk> bolker;

    public SoknadOppsummering(Soknad soknad, List<Bolk> bolker) {
        this.soknad = soknad;
        this.bolker = bolker;
    }
}
