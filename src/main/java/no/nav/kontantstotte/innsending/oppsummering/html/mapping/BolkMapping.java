package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;

import java.util.Map;
import java.util.function.BiFunction;

public abstract class BolkMapping {
    Map<String, String> tekster;
    BiFunction<String, String, Element> nyttElementMedTekstsvar;
    BiFunction<String, String, Element> nyttElementMedVerdisvar;

    BolkMapping(Map<String, String> tekster) {
        this.tekster = tekster;
        this.nyttElementMedTekstsvar = opprettElementMedTekster(tekster);
        this.nyttElementMedVerdisvar = opprettElementMedVerdier(tekster);
    }

    Bolk map(Soknad soknad, Unleash unleash) {
        return null;
    };

    public static BiFunction<String, String, Element> opprettElementMedTekster(Map<String, String> tekster) {
        return (String sporsmal, String svar) -> Element.nyttSvar(tekster.get(sporsmal), tekster.get(svar));
    }

    public static BiFunction<String, String, Element> opprettElementMedVerdier(Map<String, String> tekster) {
        return (String sporsmal, String svar) -> Element.nyttSvar(tekster.get(sporsmal), svar);
    }
}
