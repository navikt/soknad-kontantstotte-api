package no.nav.kontantstotte.innsending.oppsummering.html;

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

    public static Element nyttSvar(String sporsmal, List<String> svarListe) {
        Element element = new Element();
        element.sporsmal = sporsmal;
        element.svarListe = svarListe;
        return element;
    }

    @JsonProperty("sporsmal")
    public String sporsmal;

    @JsonProperty("svar")
    public String svar;

    @JsonProperty("advarsel")
    public String advarsel;

    @JsonProperty("svarListe")
    public List<String> svarListe;

    @JsonProperty("underelementer")
    public List<Element> underelementer;

    @Override
    public String toString() {
        return "Element{" +
                "sporsmal='" + sporsmal + '\'' +
                ", svar='" + svar + '\'' +
                ", svarListe='" + svarListe.toString() + '\'' +
                ", underelementer=" + underelementer +
                '}';
    }
}
