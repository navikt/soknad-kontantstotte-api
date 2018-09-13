package no.nav.kontantstotte.tekst;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextProviderTest {

    private TekstProvider tekstProvider = new TekstProvider("test_tekster", "nb", "nn");

    @Test
    public void that_bokmal_is_loaded() {
        assertThat(tekstProvider.hentTekster("nb").get("sprak.tekst")).isEqualTo("En tekst");
    }

    @Test
    public void that_nynorsk_is_loaded() {
        assertThat(tekstProvider.hentTekster("nn").get("sprak.tekst")).isEqualTo("Ein tekst");

    }

    @Test
    public void that_norwegian_characters_is_preserved() {
        assertThat(tekstProvider.hentTekster("nb").get("sprak.norske.tegn")).isEqualTo("æøåÆØÅ");
        assertThat(tekstProvider.hentTekster("nn").get("sprak.norske.tegn")).isEqualTo("æøåÆØÅ");

    }

}
