package no.nav.kontantstotte.oppsummering.innsending;

import no.nav.kontantstotte.oppsummering.Soknad;

public interface OppsummeringService {
    byte[] genererOppsummering(Soknad soknad);
}
