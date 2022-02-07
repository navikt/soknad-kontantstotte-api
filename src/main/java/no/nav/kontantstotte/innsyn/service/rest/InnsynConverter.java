package no.nav.kontantstotte.innsyn.service.rest;

import no.nav.kontantstotte.innsyn.domain.Barn;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonBolk;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlPersonData;

import java.util.function.Function;

class InnsynConverter {

    static Function<PdlPersonData, Person> pdlPersonDataToPerson =
            pdlPersonData -> new Person.Builder().fornavn(pdlPersonData.getNavn().get(0).getFornavn())
                                                 .mellomnavn(pdlPersonData.getNavn().get(0).getMellomnavn())
                                                 .etternavn(pdlPersonData.getNavn().get(0).getEtternavn())
                                                 .statsborgerskap(pdlPersonData.getStatsborgerskap().get(0).getLand()).build();

    static Function<PdlHentPersonBolk, Barn> pdlHentPersonBolkToBarn = barnData ->
            new Barn.Builder().fulltnavn(barnData.getPerson().getNavn().get(0).fulltNavn())
                              .fødselsnummer(barnData.getIdent())
                              .fødselsdato(barnData.getPerson().getFødsel().get(0).getFødselsdato())
                              .build();


}
