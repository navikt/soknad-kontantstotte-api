package no.nav.kontantstotte.innsyn.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonTest {

    @Test
    public void fulltnavn_ikke_produserer_mellomromsfeil_ved_null_verdier() {

        assertThat(personMedNavn("Barefornavn", null, null).getFulltnavn())
                .isEqualTo("Barefornavn");
        assertThat(personMedNavn(null, "Baremellomnavn", null).getFulltnavn())
                .isEqualTo("Baremellomnavn");
        assertThat(personMedNavn(null, null, "Bareslektsnavn").getFulltnavn())
                .isEqualTo("Bareslektsnavn");

        assertThat(personMedNavn("Fornavn", "Mellomnavn", null).getFulltnavn())
                .isEqualTo("Fornavn Mellomnavn");
        assertThat(personMedNavn("Fornavn", null, "Slektsnavn").getFulltnavn())
                .isEqualTo("Fornavn Slektsnavn");
        assertThat(personMedNavn(null, "Mellomnavn", "Slektsnavn").getFulltnavn())
                .isEqualTo("Mellomnavn Slektsnavn");

        assertThat(personMedNavn("Fornavn", "Mellomnavn", "Slektsnavn").getFulltnavn())
                .isEqualTo("Fornavn Mellomnavn Slektsnavn");
    }

    @Test
    public void fulltnavn_ikke_produserer_mellomromsfeil_ved_blanke_verdier() {

        assertThat(personMedNavn("Barefornavn", "", "").getFulltnavn())
                .isEqualTo("Barefornavn");
        assertThat(personMedNavn("", "Baremellomnavn", "").getFulltnavn())
                .isEqualTo("Baremellomnavn");
        assertThat(personMedNavn("", "", "Bareslektsnavn").getFulltnavn())
                .isEqualTo("Bareslektsnavn");

        assertThat(personMedNavn("Fornavn", "Mellomnavn", "").getFulltnavn())
                .isEqualTo("Fornavn Mellomnavn");
        assertThat(personMedNavn("Fornavn", "", "Slektsnavn").getFulltnavn())
                .isEqualTo("Fornavn Slektsnavn");
        assertThat(personMedNavn("", "Mellomnavn", "Slektsnavn").getFulltnavn())
                .isEqualTo("Mellomnavn Slektsnavn");

    }

    private Person personMedNavn(String fornavn, String mellonmavn, String slektsnavn) {
        return new Person.Builder().fornavn(fornavn).mellomnavn(mellonmavn).etternavn(slektsnavn).build();
    }
}
