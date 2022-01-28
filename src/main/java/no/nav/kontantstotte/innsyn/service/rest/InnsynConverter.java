package no.nav.kontantstotte.innsyn.service.rest;

import no.nav.kontantstotte.innsyn.domain.Barn;
import no.nav.kontantstotte.innsyn.domain.FortroligAdresseException;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonBolk;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlPersonData;
import no.nav.tps.innsyn.KodeDto;
import no.nav.tps.innsyn.KodeMedDatoOgKildeDto;
import no.nav.tps.innsyn.PersoninfoDto;
import no.nav.tps.innsyn.RelasjonDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

class InnsynConverter {

    private enum SpesiellOpplysning {
        KODE6("SPSF"),
        KODE7("SPFO");

        private final String tpsKode;

        SpesiellOpplysning(String tpsKode) {
            this.tpsKode = tpsKode;
        }

        static Optional<SpesiellOpplysning> fraTpsKode(String tpsKode) {
            return Arrays.stream(SpesiellOpplysning.values())
                         .filter(value -> value.tpsKode.equals(tpsKode))
                         .findFirst();
        }
    }

    static final DateTimeFormatter DATO_PATTERN_SOKNAD = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    static Function<PersoninfoDto, Person> personinfoDtoToPerson = dto -> {

        Optional.ofNullable(dto.getSpesiellOpplysning())
                .map(KodeMedDatoOgKildeDto::getKode)
                .map(KodeDto::getVerdi)
                .flatMap(SpesiellOpplysning::fraTpsKode)
                .ifPresent((kode) -> {
                    throw new FortroligAdresseException("Personen er registrert med spesreg, " + kode);
                });

        return new Person.Builder()
                .fornavn(dto.getNavn().getFornavn())
                .mellomnavn(dto.getNavn().getMellomnavn())
                .etternavn(dto.getNavn().getSlektsnavn())
                .statsborgerskap(dto.getStatsborgerskap().getKode().getVerdi())
                .build();
    };

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


    static Function<RelasjonDto, Barn> relasjonDtoToBarn = dto -> new Barn.Builder()
            .fødselsnummer(dto.getIdent())
            .fulltnavn(dto.getForkortetNavn())
            .fødselsdato(LocalDate.parse(dto.getFoedselsdato()).format(DATO_PATTERN_SOKNAD))
            .build();
}
