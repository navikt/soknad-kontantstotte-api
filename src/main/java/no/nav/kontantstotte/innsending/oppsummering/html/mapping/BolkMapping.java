package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class BolkMapping {
    Map<String, String> tekster;
    Function<Tekstnokkel, Element> nyttElementMedSvar;
    BiFunction<Tekstnokkel, Tekstnokkel, Element> nyttElementMedTekstsvar;
    BiFunction<Tekstnokkel, String, Element> nyttElementMedVerdisvar;

    BolkMapping(Map<String, String> tekster) {
        this.tekster = tekster;
        this.nyttElementMedSvar = opprettElementMedSvar(tekster);
        this.nyttElementMedTekstsvar = opprettElementMedTekster(tekster);
        this.nyttElementMedVerdisvar = opprettElementMedVerdier(tekster);
    }


    abstract Bolk map(Soknad soknad);

    public static Function<Tekstnokkel, Element> opprettElementMedSvar(Map<String, String> tekster) {
        return (Tekstnokkel svar) -> {
            Element element = new Element();
            element.svar = tekster.get(svar.getNokkel());
            return element;
        };
    }

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
