package no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Bolk;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Element;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering;

import java.util.Map;
import java.util.function.BiFunction;

public abstract class BolkMapping {
    Map<String, String> tekster;
    BiFunction<String, String, Element> nyttElementMedTekstsvar;
    BiFunction<String, String, Element> nyttElementMedVerdisvar;

    BolkMapping(Map<String, String> tekster) {
        this.tekster = tekster;
        this.nyttElementMedTekstsvar = SoknadTilOppsummering.opprettElementMedTekster(tekster);
        this.nyttElementMedVerdisvar = SoknadTilOppsummering.opprettElementMedVerdier(tekster);
    }

    Bolk map(Soknad soknad, Unleash unleash) {
        return null;
    };
}
