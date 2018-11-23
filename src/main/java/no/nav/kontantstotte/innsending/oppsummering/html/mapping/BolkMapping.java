package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;

import java.util.Map;

public abstract class BolkMapping {
    private Map<String, String> tekster;

    BolkMapping(Map<String, String> tekster) {
        this.tekster = tekster;
    }


    abstract Bolk map(Soknad soknad);

    Element svar(Tekstnokkel tekstnokkel) {
        Element element = new Element();
        element.svar = tekst(tekstnokkel);
        return element;
    }

    Element svar(Tekstnokkel sporsmal, Tekstnokkel svar) {
        return Element.nyttSvar(tekst(sporsmal), tekst(svar));
    }

    Element svar(Tekstnokkel sporsmal, String svar) {
        return Element.nyttSvar(tekst(sporsmal), svar);
    }

    Element svar(Tekstnokkel sporsmal, Integer svar) {
        return svar(sporsmal, svar.toString());
    }

    Element svar(Tekstnokkel sporsmal, Tekstnokkel svar, Tekstnokkel advarsel) {
        return Element.nyttSvar(tekst(sporsmal), tekst(svar), tekst(advarsel));
    }

    Element svar(Tekstnokkel sporsmal, String svar, Tekstnokkel advarsel) {
        return Element.nyttSvar(tekst(sporsmal), svar, tekst(advarsel));
    }

    Element svar(Tekstnokkel sporsmal, Integer svar, Tekstnokkel advarsel) {
        return Element.nyttSvar(tekst(sporsmal), svar.toString(), tekst(advarsel));
    }

    String tekst(Tekstnokkel tekstnokkel) {
        return tekster.get(tekstnokkel.getNokkel());
    }
}
