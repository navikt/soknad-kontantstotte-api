package no.nav.kontantstotte.innsending;

import no.nav.kontantstotte.innsending.steg.Oppsummering;
import no.nav.kontantstotte.innsending.steg.Veiledning;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SoknadTest {

    @Test
    public void at_soknad_er_gyldig_ved_bekreftelse() {
        Soknad soknad = new Soknad();
        soknad.oppsummering = new Oppsummering();
        soknad.oppsummering.bekreftelse = "JA";
        soknad.veiledning = new Veiledning();
        soknad.veiledning.bekreftelse = "JA";
        assertThat(soknad.harBekreftetOpplysninger()).isTrue();
        assertThat(soknad.harBekreftetPlikter()).isTrue();
    }

    @Test
    public void at_soknad_er_ugyldig_uten_bekreftelse() {
        Soknad soknad = new Soknad();
        assertThat(soknad.harBekreftetOpplysninger()).isFalse();
        assertThat(soknad.harBekreftetPlikter()).isFalse();
    }

}
