package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.SvarElement;
import no.nav.kontantstotte.innsending.oppsummering.html.VedleggElement;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class BolkMapping {
    Tekster tekster;
    Function<Tekstnokkel, SvarElement> nyttElementMedSvar;
    BiFunction<Tekstnokkel, Tekstnokkel, SvarElement> nyttElementMedTekstsvar;
    BiFunction<Tekstnokkel, String, SvarElement> nyttElementMedVerdisvar;
    BiFunction<Tekstnokkel, List<String>, VedleggElement> nyttElementMedListe;
    static boolean brukFlertall;

    BolkMapping(Tekster tekster) {
        this.tekster = tekster;
        this.nyttElementMedSvar = opprettElementMedSvar(tekster);
        this.nyttElementMedTekstsvar = opprettElementMedTekster(tekster);
        this.nyttElementMedVerdisvar = opprettElementMedVerdier(tekster);
        this.nyttElementMedListe = opprettElementMedListe(tekster);
    }

    abstract Bolk map(Soknad soknad);

    public static Function<Tekstnokkel, SvarElement> opprettElementMedSvar(Tekster tekster) {
        return (Tekstnokkel svar) -> SvarElement.nyttSvar(tekster.hentTekst(svar.getNokkel(), brukFlertall));
    }

    public static BiFunction<Tekstnokkel, Tekstnokkel, SvarElement> opprettElementMedTekster(Tekster tekster) {
        return (Tekstnokkel sporsmal, Tekstnokkel svar) ->
                SvarElement.nyttSvar(
                        tekster.hentTekst(sporsmal.getNokkel(), brukFlertall),
                        tekster.hentTekst(svar.getNokkel(), brukFlertall));
    }

    public static BiFunction<Tekstnokkel, String, SvarElement> opprettElementMedVerdier(Tekster tekster) {
        return (Tekstnokkel sporsmal, String svar) ->
                SvarElement.nyttSvar(
                        tekster.hentTekst(sporsmal.getNokkel(), brukFlertall),
                        svar);
    }

    public static BiFunction<Tekstnokkel, List<String>, VedleggElement> opprettElementMedListe(Tekster tekster) {
        return (Tekstnokkel sporsmal, List<String> list) ->
                VedleggElement.nyttSvar(
                        tekster.hentTekst(sporsmal.getNokkel(), brukFlertall),
                        list
                );
    }

    public void setBrukFlertall(boolean brukFlertall) {
        this.brukFlertall = brukFlertall;
    }
}
