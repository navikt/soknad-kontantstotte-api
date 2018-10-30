package no.nav.kontantstotte.person.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonTest {

    @Test
    public void fulltnavn_ikke_produserer_mellomromsfeil_ved_null_verdier() {

        assertThat(personMedNavn("Barefornavn", null, null).getNavn())
                .isEqualTo("Barefornavn");
        assertThat(personMedNavn(null, "Baremellomnavn", null).getNavn())
                .isEqualTo("Baremellomnavn");
        assertThat(personMedNavn(null, null, "Bareslektsnavn").getNavn())
                .isEqualTo("Bareslektsnavn");

        assertThat(personMedNavn("Fornavn", "Mellomnavn", null).getNavn())
                .isEqualTo("Fornavn Mellomnavn");
        assertThat(personMedNavn("Fornavn", null, "Slektsnavn").getNavn())
                .isEqualTo("Fornavn Slektsnavn");
        assertThat(personMedNavn(null, "Mellomnavn", "Slektsnavn").getNavn())
                .isEqualTo("Mellomnavn Slektsnavn");

        assertThat(personMedNavn("Fornavn", "Mellomnavn", "Slektsnavn").getNavn())
                .isEqualTo("Fornavn Mellomnavn Slektsnavn");
    }

    @Test
    public void fulltnavn_ikke_produserer_mellomromsfeil_ved_blanke_verdier() {

        assertThat(personMedNavn("Barefornavn", "", "").getNavn())
                .isEqualTo("Barefornavn");
        assertThat(personMedNavn("", "Baremellomnavn", "").getNavn())
                .isEqualTo("Baremellomnavn");
        assertThat(personMedNavn("", "", "Bareslektsnavn").getNavn())
                .isEqualTo("Bareslektsnavn");

        assertThat(personMedNavn("Fornavn", "Mellomnavn", "").getNavn())
                .isEqualTo("Fornavn Mellomnavn");
        assertThat(personMedNavn("Fornavn", "", "Slektsnavn").getNavn())
                .isEqualTo("Fornavn Slektsnavn");
        assertThat(personMedNavn("", "Mellomnavn", "Slektsnavn").getNavn())
                .isEqualTo("Mellomnavn Slektsnavn");

    }

    private Person personMedNavn(String fornavn, String mellonmavn, String slektsnavn) {
        return new Person.Builder().fornavn(fornavn).mellomnavn(mellonmavn).slektsnavn(slektsnavn).build();
    }
}
