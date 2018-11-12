package no.nav.kontantstotte.person.service.rest;

import no.nav.kontantstotte.person.domain.Person;
import no.nav.kontantstotte.person.domain.FortroligAdresseException;
import no.nav.tps.person.KodeDto;
import no.nav.tps.person.KodeMedDatoOgKildeDto;
import no.nav.tps.person.PersoninfoDto;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

class PersonConverter {

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
                    throw new FortroligAdresseException("Personen er registrert med spesreg, " + kode);
                });

        return new Person.Builder()
                .fornavn(dto.getNavn().getFornavn())
                .mellomnavn(dto.getNavn().getMellomnavn())
                .slektsnavn(dto.getNavn().getSlektsnavn())
                .build();
    };



}
