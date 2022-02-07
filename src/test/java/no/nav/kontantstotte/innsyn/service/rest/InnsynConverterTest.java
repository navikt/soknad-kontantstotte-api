package no.nav.kontantstotte.innsyn.service.rest;

import no.nav.kontantstotte.innsyn.domain.Barn;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlFødselsdato;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonBolk;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlNavn;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlPersonData;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlStatsborgerskap;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.newArrayList;

public class InnsynConverterTest {

    private static final String FORNAVN = "fornavn";
    private static final String MELLOMNAVN = "mellomnavn";
    private static final String ETTERNAVN = "etternavn";

    @Test
    public void at_pdlPersonData_konverteres_til_Person() {
        Person person = InnsynConverter.pdlPersonDataToPerson.apply(lagPdlPersonData(false));

        assertThat(person.getFornavn()).isEqualTo(FORNAVN);
        assertThat(person.getMellomnavn()).isEqualTo(MELLOMNAVN);
        assertThat(person.getEtternavn()).isEqualTo(ETTERNAVN);
        assertThat(person.getFulltnavn()).isEqualTo(String.join(" ", FORNAVN, MELLOMNAVN, ETTERNAVN));
        assertThat(person.getStatsborgerskap()).isEqualTo("NOR");
    }

    @Test
    public void at_pdlHentPersonBolk_konverteres_til_Barn() {
        PdlHentPersonBolk pdlHentPersonBolk = new PdlHentPersonBolk("testident", lagPdlPersonData(true));
        Barn barn = InnsynConverter.pdlHentPersonBolkToBarn.apply(pdlHentPersonBolk);

        assertThat(barn.getFulltnavn()).isEqualTo(String.join(" ", FORNAVN, MELLOMNAVN, ETTERNAVN));
        assertThat(barn.getFødselsdato()).isEqualTo("testfødselsdato");
        assertThat(barn.getFødselsnummer()).isEqualTo("testident");
    }

    private PdlPersonData lagPdlPersonData(boolean erFødselFinnes) {
        return new PdlPersonData(erFødselFinnes ? newArrayList(new PdlFødselsdato("testfødselsdato")) : emptyList(),
                                 newArrayList(new PdlNavn(FORNAVN, MELLOMNAVN, ETTERNAVN)),
                                 emptyList(),
                                 newArrayList(new PdlStatsborgerskap("NOR")),
                                 emptyList());
    }
}
