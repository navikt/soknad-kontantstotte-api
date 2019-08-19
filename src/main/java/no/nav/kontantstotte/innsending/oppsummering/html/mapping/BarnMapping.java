package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Barn;

import java.util.ArrayList;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;


public class BarnMapping extends BolkMapping {
    public BarnMapping(Tekster tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        Bolk barneBolk = new Bolk();
        Barn barn = soknad.mineBarn;

        barneBolk.tittel = tekster.hentTekst(BARN_TITTEL.getNokkel());
        barneBolk.undertittel = tekster.hentTekst(BARN_UNDERTITTEL.getNokkel());
        barneBolk.elementer = new ArrayList<>();

        Element innsendtBarn = "JA".equalsIgnoreCase(barn.erFlerling)
                ? Element.nyttSvar(
                tekster.hentTekst(BARN_FØDSELSDATO.getNokkel()), barn.fødselsdato,
                tekster.hentTekst(BARN_ADVARSEL.getNokkel()))
                : nyttElementMedVerdisvar.apply(BARN_FØDSELSDATO, barn.fødselsdato);
        barneBolk.elementer.add(nyttElementMedVerdisvar.apply(BARN_NAVN, barn.navn));
        barneBolk.elementer.add(innsendtBarn);

        return barneBolk;
    }
}
