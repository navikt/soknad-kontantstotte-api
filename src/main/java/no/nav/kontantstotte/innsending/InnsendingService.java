package no.nav.kontantstotte.innsending;

import no.nav.familie.ks.kontrakter.søknad.Søknad;

public interface InnsendingService {

    Søknad sendInnSøknad(Søknad søknad);
}
