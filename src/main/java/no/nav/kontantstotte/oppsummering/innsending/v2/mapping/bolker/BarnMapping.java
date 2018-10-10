package no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barn;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Bolk;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Element;

import java.util.ArrayList;
import java.util.Map;

public class BarnMapping implements BolkMapping {
    public static final String BARN_TITTEL = "barn.tittel";
    public static final String BARN_UNDERTITTEL = "oppsummering.barn.subtittel";
    public static final String BARN_NAVN = "barn.navn";
    public static final String BARN_FODSELSDATO = "barn.fodselsdato";

    @Override
    public Bolk map(Soknad soknad, Map<String, String> tekster, Unleash unleash) {
        Bolk barneBolk = new Bolk();
        Barn barn = soknad.mineBarn;

        barneBolk.tittel = tekster.get(BARN_TITTEL);
        barneBolk.undertittel = tekster.get(BARN_UNDERTITTEL);
        barneBolk.elementer = new ArrayList<>();
        barneBolk.elementer.add(Element.nyttSvar(tekster.get(BARN_NAVN), barn.navn));
        barneBolk.elementer.add(Element.nyttSvar(tekster.get(BARN_FODSELSDATO), barn.fodselsdato));
        return barneBolk;
    }
}
