package no.nav.kontantstotte.innsending;

import no.nav.familie.kontrakter.ks.søknad.Søknad;

public class SamletInnsendingDto {
    public Soknad soknad;
    public Søknad kontraktSøknad;

    public SamletInnsendingDto(Soknad soknad, Søknad søknad) {
        this.soknad = soknad;
        this.kontraktSøknad = søknad;
    }
}
