package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class BolkMapping {
    Tekster tekster;
    Function<Tekstnokkel, Element> nyttElementMedSvar;
    BiFunction<Tekstnokkel, Tekstnokkel, Element> nyttElementMedTekstsvar;
    BiFunction<Tekstnokkel, String, Element> nyttElementMedVerdisvar;
    BiFunction<Tekstnokkel, List<String>, Element> nyttElementMedListe;
    static boolean brukFlertall;

    BolkMapping(Tekster tekster) {
        this.tekster = tekster;
        this.nyttElementMedSvar = opprettElementMedSvar(tekster);
        this.nyttElementMedTekstsvar = opprettElementMedTekster(tekster);
        this.nyttElementMedVerdisvar = opprettElementMedVerdier(tekster);
        this.nyttElementMedListe = opprettElementMedListe(tekster);
    }

    abstract Bolk map(Soknad soknad);

    public static Function<Tekstnokkel, Element> opprettElementMedSvar(Tekster tekster) {
        return (Tekstnokkel svar) -> Element.nyttSvar(tekster.hentTekst(svar.getNokkel(), brukFlertall));
    }

    public static BiFunction<Tekstnokkel, Tekstnokkel, Element> opprettElementMedTekster(Tekster tekster) {
        return (Tekstnokkel sporsmal, Tekstnokkel svar) ->
                Element.nyttSvar(
                        tekster.hentTekst(sporsmal.getNokkel(), brukFlertall), tekster.hentTekst(svar.getNokkel(), brukFlertall)
                );
    }

    public static BiFunction<Tekstnokkel, String, Element> opprettElementMedVerdier(Tekster tekster) {
        return (Tekstnokkel sporsmal, String svar) ->
                Element.nyttSvar(
                        tekster.hentTekst(sporsmal.getNokkel(), brukFlertall), svar
                );
    }

    public static BiFunction<Tekstnokkel, List<String>, Element> opprettElementMedListe(Tekster tekster) {
        return (Tekstnokkel sporsmal, List<String> list) ->
                Element.nyttSvar(
                        tekster.hentTekst(sporsmal.getNokkel(), brukFlertall), list
                );
    }

    public void setBrukFlertall(boolean brukFlertall) {
        this.brukFlertall = brukFlertall;
    }
}
