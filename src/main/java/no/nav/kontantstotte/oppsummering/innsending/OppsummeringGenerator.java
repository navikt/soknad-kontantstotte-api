package no.nav.kontantstotte.oppsummering.innsending;

import no.nav.kontantstotte.oppsummering.Soknad;

public interface OppsummeringGenerator {
    byte[] genererOppsummering(Soknad soknad, String fnr);
}
