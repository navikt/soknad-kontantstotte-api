package no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barn;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Bolk;

import java.util.ArrayList;
import java.util.Map;

import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Tekstnokkel.*;

public class BarnMapping extends BolkMapping {
    public BarnMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad, Unleash unleash) {
        Bolk barneBolk = new Bolk();
        Barn barn = soknad.mineBarn;

        barneBolk.tittel = tekster.get(BARN_TITTEL.getNokkel());
        barneBolk.undertittel = tekster.get(BARN_UNDERTITTEL.getNokkel());
        barneBolk.elementer = new ArrayList<>();
        barneBolk.elementer.add(nyttElementMedVerdisvar.apply(BARN_NAVN.getNokkel(), barn.navn));
        barneBolk.elementer.add(nyttElementMedVerdisvar.apply(BARN_FODSELSDATO.getNokkel(), barn.fodselsdato));
        return barneBolk;
    }
}
