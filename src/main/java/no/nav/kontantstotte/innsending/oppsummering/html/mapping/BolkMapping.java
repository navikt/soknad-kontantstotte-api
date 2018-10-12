package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;

import java.util.Map;
import java.util.function.BiFunction;

public abstract class BolkMapping {
    Map<String, String> tekster;
    BiFunction<Tekstnokkel, Tekstnokkel, Element> nyttElementMedTekstsvar;
    BiFunction<Tekstnokkel, String, Element> nyttElementMedVerdisvar;

    BolkMapping(Map<String, String> tekster) {
        this.tekster = tekster;
        this.nyttElementMedTekstsvar = opprettElementMedTekster(tekster);
        this.nyttElementMedVerdisvar = opprettElementMedVerdier(tekster);
    }


    abstract Bolk map(Soknad soknad, Unleash unleash);

    public static BiFunction<Tekstnokkel, Tekstnokkel, Element> opprettElementMedTekster(Map<String, String> tekster) {
        return (Tekstnokkel sporsmal, Tekstnokkel svar) ->
                Element.nyttSvar(
                        tekster.get(sporsmal.getNokkel()),
                        tekster.get(svar.getNokkel()));
    }

    public static BiFunction<Tekstnokkel, String, Element> opprettElementMedVerdier(Map<String, String> tekster) {
        return (Tekstnokkel sporsmal, String svar) ->
                Element.nyttSvar(
                        tekster.get(sporsmal.getNokkel()),
                        svar);
    }
}
