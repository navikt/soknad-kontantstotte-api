package no.nav.kontantstotte.innsending;

import no.nav.kontantstotte.innsending.steg.Oppsummering;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SoknadTest {

    @Test
    public void at_soknad_er_gyldig_ved_bekreftelse() {
        Soknad soknad = new Soknad.Builder()
                .oppsummering(new Oppsummering("JA"))
                .build();
        assertThat(soknad.erGyldig()).isTrue();
    }

    @Test
    public void at_soknad_er_ugyldig_uten_bekreftelse() {
        Soknad soknad = new Soknad.Builder().build();
        assertThat(soknad.erGyldig()).isFalse();
    }

}
