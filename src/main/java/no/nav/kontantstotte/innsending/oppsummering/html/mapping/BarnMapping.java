package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Barn;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
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

    public Bolk mapNy(Søknad søknad) {
        Bolk barneBolk = new Bolk();
        Set<no.nav.familie.ks.kontrakter.søknad.Barn> barn = søknad.getOppgittFamilieforhold().getBarna();

        barneBolk.tittel = tekster.hentTekst(BARN_TITTEL.getNokkel());
        barneBolk.undertittel = tekster.hentTekst(BARN_UNDERTITTEL.getNokkel());
        barneBolk.elementer = new ArrayList<>();

        List<String> navn = barn.stream()
                .map(no.nav.familie.ks.kontrakter.søknad.Barn::getNavn).collect(toList());
        List<String> fødselsdatoer = barn.stream().map(no.nav.familie.ks.kontrakter.søknad.Barn::getFødselsnummer)
                .map(fnr -> fnr.substring(0, 6)).sorted()
                .collect(toList());

        String formatertNavn = String.join(" og ", navn);
        String formatertFdato = String.join(" og ", fødselsdatoer);

        Element innsendtBarn = barn.size() > 1
                ? Element.nyttSvar(
                tekster.hentTekst(BARN_FØDSELSDATO.getNokkel()), formatertFdato,
                tekster.hentTekst(BARN_ADVARSEL.getNokkel()))
                : nyttElementMedVerdisvar.apply(BARN_FØDSELSDATO, formatertFdato);
        barneBolk.elementer.add(nyttElementMedVerdisvar.apply(BARN_NAVN, formatertNavn));
        barneBolk.elementer.add(innsendtBarn);

        return barneBolk;
    }
}
