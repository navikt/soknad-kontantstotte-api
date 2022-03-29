package no.nav.kontantstotte.innsending;

import no.nav.familie.kontrakter.ks.søknad.Søknad;

public interface InnsendingService {

    Søknad sendInnSøknad(Søknad søknad);

    String ping();
}
