package no.nav.kontantstotte.person.service.rest;

import no.nav.kontantstotte.person.domain.Adresse;
import no.nav.tps.person.BoadresseDto;

import java.util.function.Function;

class AdresseConverter {

    static Function<BoadresseDto, Adresse> boadresseDtoToBoadresse = dto ->
            new Adresse.Builder()
                    .adresse(dto.getAdresse())
                    .adressetillegg(dto.getAdressetillegg())
                    .bydel(dto.getBydel())
                    .kommune(dto.getKommune())
                    .landkode(dto.getLandkode())
                    .postnummer(dto.getPostnummer()).build();
}
