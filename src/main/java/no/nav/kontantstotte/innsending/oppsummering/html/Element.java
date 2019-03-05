package no.nav.kontantstotte.innsending.oppsummering.html;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import no.nav.kontantstotte.innsending.oppsummering.ElementJsonDeserializer;

import java.util.List;

@JsonDeserialize(using = ElementJsonDeserializer.class)
public class Element<T> {

    public Element(String sporsmal, T svar, String advarsel, List<Element> underelementer) {
        this.svar = svar;
        this.sporsmal = sporsmal;
        this.advarsel = advarsel;
        this.underelementer = underelementer;
    }

    public static Element<String> nyttSvar(String svar) {
        return new Element<>(null, svar, null, null);
    }

    public static Element<String> nyttSvar(String sporsmal, String svar) {
        return new Element<>(sporsmal, svar, null, null);
    }

    public static Element<String> nyttSvar(String sporsmal, String svar, String advarsel) {
        return new Element<>(sporsmal, svar, advarsel, null);
    }

    public static Element<List<String>> nyttSvar(String sporsmal, List<String> svar) {
        return new Element<>(sporsmal, svar, null , null);
    }

    @JsonProperty("sporsmal")
    public String sporsmal;

    @JsonProperty("svar")
    public T svar;

    @JsonProperty("advarsel")
    public String advarsel;

    @JsonProperty("underelementer")
    public List<Element> underelementer;


    @Override
    public String toString() {
        return "SvarElement{" +
                "sporsmal='" + sporsmal + '\'' +
                ", svar='" + svar.toString() + '\'' +
                ", underelementer=" + underelementer +
                '}';
    }
}
