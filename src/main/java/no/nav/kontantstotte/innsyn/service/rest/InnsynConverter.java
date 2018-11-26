package no.nav.kontantstotte.innsyn.service.rest;

import no.nav.kontantstotte.innsyn.domain.Barn;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.kontantstotte.innsyn.domain.SkjermetAdresseException;
import no.nav.tps.innsyn.KodeDto;
import no.nav.tps.innsyn.KodeMedDatoOgKildeDto;
import no.nav.tps.innsyn.PersoninfoDto;
import no.nav.tps.innsyn.RelasjonDto;

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

    static Function<PersoninfoDto, Person> personinfoDtoToPerson = dto -> {

        Optional.ofNullable(dto.getSpesiellOpplysning())
                .map(KodeMedDatoOgKildeDto::getKode)
                .map(KodeDto::getVerdi)
                .flatMap(SpesiellOpplysning::fraTpsKode)
                .ifPresent((kode) -> {
                    throw new SkjermetAdresseException("Personen er registrert med spesreg, " + kode);
                });

        return new Person.Builder()
                .fornavn(dto.getNavn().getFornavn())
                .mellomnavn(dto.getNavn().getMellomnavn())
                .slektsnavn(dto.getNavn().getSlektsnavn())
                .build();
    };

    static Function<RelasjonDto, Barn> relasjonDtoToBarn = dto -> new Barn.Builder()
                .fodselsnummer(dto.getIdent())
                .fulltnavn(dto.getForkortetNavn())
                .fodselsdato(dto.getFoedselsdato())
                .build();
}
