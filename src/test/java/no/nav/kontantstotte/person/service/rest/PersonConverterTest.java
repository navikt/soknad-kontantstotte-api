package no.nav.kontantstotte.person.service.rest;

import no.nav.kontantstotte.person.domain.Person;
import no.nav.kontantstotte.person.domain.FortroligAdresseException;
import no.nav.tps.person.KodeDto;
import no.nav.tps.person.KodeMedDatoOgKildeDto;
import no.nav.tps.person.NavnDto;
import no.nav.tps.person.PersoninfoDto;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.rules.ExpectedException.none;

public class PersonConverterTest {

    @Rule
    public ExpectedException exception = none();

    @Test
    public void at_kode6_kaster_exception() {
        exception.expect(FortroligAdresseException.class);

        PersoninfoDto dto = person1();
        dto.setSpesiellOpplysning(spesiellOpplysning("SPSF"));

        PersonConverter.personinfoDtoToPerson.apply(dto);
    }

    @Test
    public void at_kode7_kaster_exception() {
        exception.expect(FortroligAdresseException.class);

        PersoninfoDto dto = person1();
        dto.setSpesiellOpplysning(spesiellOpplysning("SPFO"));

        PersonConverter.personinfoDtoToPerson.apply(dto);
    }

    @Test
    public void at_annen_kode_enn_6_og_7_ikke_kaster_exception() {

        PersoninfoDto dto = person1();
        dto.setSpesiellOpplysning(spesiellOpplysning("UFB"));

        Person p = PersonConverter.personinfoDtoToPerson.apply(dto);

        assertThat(p.getFornavn()).isEqualTo(dto.getNavn().getFornavn());
        assertThat(p.getMellomnavn()).isEqualTo(dto.getNavn().getMellomnavn());
        assertThat(p.getSlektsnavn()).isEqualTo(dto.getNavn().getSlektsnavn());

    }

    @Test
    public void at_personer_uten_spesiell_opplysning_konverteres() {

        PersoninfoDto dto = person1();
        Person p = PersonConverter.personinfoDtoToPerson.apply(dto);

        assertThat(p.getFornavn()).isEqualTo(dto.getNavn().getFornavn());
        assertThat(p.getMellomnavn()).isEqualTo(dto.getNavn().getMellomnavn());
        assertThat(p.getSlektsnavn()).isEqualTo(dto.getNavn().getSlektsnavn());
    }

    private KodeMedDatoOgKildeDto spesiellOpplysning(String kodeVerdi) {
        KodeMedDatoOgKildeDto dto = new KodeMedDatoOgKildeDto();

        KodeDto kode = new KodeDto();
        kode.setVerdi(kodeVerdi);
        dto.setKode(kode);

        return dto;
    }

    private PersoninfoDto person1() {
        PersoninfoDto dto = new PersoninfoDto();
        NavnDto navn = new NavnDto();
        navn.setFornavn("fornavn1");
        navn.setSlektsnavn("etternavn1");
        dto.setNavn(navn);
        return dto;
    }


}
