package no.nav.kontantstotte.innsending;

import no.nav.familie.ks.kontrakter.søknad.Søknad;

public class SamletInnsendingDto {
    public Soknad soknad;
    public Søknad kontraktSøknad;

    public SamletInnsendingDto(Soknad soknad, Søknad søknad) {
        this.soknad = soknad;
        this.kontraktSøknad = søknad;
    }
}
