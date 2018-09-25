package no.nav.kontantstotte.oppsummering;

import no.nav.kontantstotte.oppsummering.bolk.Oppsummering;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SoknadTest {

    @Test
    public void at_soknad_er_gyldig_ved_bekreftelse() {
        Soknad soknad = new Soknad();
        soknad.oppsummering = new Oppsummering();
        soknad.oppsummering.bekreftelse = "JA";
        assertThat(soknad.erGyldig()).isTrue();
    }

    @Test
    public void at_soknad_er_ugyldig_uten_bekreftelse() {
        Soknad soknad = new Soknad();
        assertThat(soknad.erGyldig()).isFalse();
    }

}
