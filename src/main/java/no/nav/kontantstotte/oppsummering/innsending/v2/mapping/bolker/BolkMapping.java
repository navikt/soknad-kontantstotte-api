package no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Bolk;

import java.util.Map;

public interface BolkMapping {
    Bolk map(Soknad soknad, Map<String, String> tekster, Unleash unleash);
}
