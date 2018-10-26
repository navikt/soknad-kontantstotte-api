package no.nav.kontantstotte.person.service.rest;

import no.nav.kontantstotte.person.domain.Person;
import no.nav.kontantstotte.person.domain.SikkerhetsbegrensningException;
import no.nav.tps.person.KodeMedDatoOgKildeDto;
import no.nav.tps.person.PersoninfoDto;

import java.util.Optional;
import java.util.function.Function;

class PersonConverter {

    static Function<PersoninfoDto, Person> personinfoDtoToPerson = dto -> {

        if(Optional.ofNullable(dto.getSpesiellOpplysning())
                .map(KodeMedDatoOgKildeDto::getKode).isPresent()) {
            throw new SikkerhetsbegrensningException("Personen er registrert med spesreg");
        }

        return new Person.Builder()
                .fornavn(dto.getNavn().getFornavn())
                .mellomnavn(dto.getNavn().getMellomnavn())
                .slektsnavn(dto.getNavn().getSlektsnavn())
                .build();
    };



}
