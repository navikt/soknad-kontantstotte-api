package no.nav.kontantstotte.innsending.oppsummering.html;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import no.nav.kontantstotte.innsending.oppsummering.ElementJsonDeserializer;

import java.util.List;

@JsonDeserialize(using = ElementJsonDeserializer.class)
public abstract class Element {

    public Element(String sporsmal, String advarsel, List<Element> underelementer) {
        this.sporsmal = sporsmal;
        this.advarsel = advarsel;
        this.underelementer = underelementer;
    }

    @JsonProperty("sporsmal")
    public String sporsmal;

    @JsonProperty("advarsel")
    public String advarsel;

    @JsonProperty("underelementer")
    public List<Element> underelementer;

}
