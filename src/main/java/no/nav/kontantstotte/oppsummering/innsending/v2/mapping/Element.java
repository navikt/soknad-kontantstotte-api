package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Element {

    public static Element nyttSvar(String sporsmal, String svar){
        Element element = new Element();
        element.sporsmal = sporsmal;
        element.svar = svar;
        return element;
    }

    public static Element nyttSvar(String sporsmal, String svar, String advarsel){
        Element element = nyttSvar(sporsmal, svar);
        element.advarsel = advarsel;
        return element;
    }

    @JsonProperty("sporsmal")
    public String sporsmal;

    @JsonProperty("svar")
    public String svar;

    @JsonProperty("advarsel")
    public String advarsel;

    @JsonProperty("underelementer")
    public List<Element> underelementer;

}
