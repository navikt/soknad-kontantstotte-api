package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.steg.Barn;

import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;


public class BarnMapping extends BolkMapping {
    public BarnMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        Barn barn = soknad.getMineBarn();

        return new Bolk()
                .medTittel(tekst(BARN_TITTEL))
                .medUndertittel(tekst(BARN_UNDERTITTEL))
                .add(svar(BARN_NAVN, barn.navn))
                .add(svar(BARN_FODSELSDATO, barn.fodselsdato));

    }
}
