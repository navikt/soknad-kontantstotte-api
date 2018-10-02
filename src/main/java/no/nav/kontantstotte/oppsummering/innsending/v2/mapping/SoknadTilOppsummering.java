package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;


import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SoknadTilOppsummering {


    public static final String BARN_TITTEL = "barn.tittel";
    public static final String BARN_UNDERTITTEL = "oppsummering.barn.subtittel";
    public static final String BARN_NAVN = "barn.navn";
    public static final String BARN_FODSELSDATO = "barn.fodselsdato";

    public SoknadOppsummering map(Soknad soknad, Map<String, String> tekster) {

        Barn mineBarn = soknad.mineBarn;

        Bolk barneBolk = mapBarn(mineBarn, tekster);


        return new SoknadOppsummering(soknad, Arrays.asList(barneBolk), tekster);
    }

    public Bolk mapBarn(Barn barn, Map<String, String> tekster) {
        Bolk barneBolk = new Bolk();
        barneBolk.bolknavn = "minebarn";
        barneBolk.tittel = tekster.get(BARN_TITTEL);
        barneBolk.undertittel = tekster.get(BARN_UNDERTITTEL);
        barneBolk.elementer = new ArrayList<>();
        barneBolk.elementer.add(Element.nyttSvar(tekster.get(BARN_NAVN), barn.navn));
        barneBolk.elementer.add(Element.nyttSvar(tekster.get(BARN_FODSELSDATO), barn.fodselsdato));
        return barneBolk;
    }


}
