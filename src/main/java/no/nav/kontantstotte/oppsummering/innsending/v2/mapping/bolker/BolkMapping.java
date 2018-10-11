package no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Bolk;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Element;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Tekstnokkel;

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
