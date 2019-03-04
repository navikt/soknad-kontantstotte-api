package no.nav.kontantstotte.innsending.oppsummering.html;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class VedleggElement extends Element {

    @JsonCreator
    public VedleggElement(
            @JsonProperty("svar") List<String> svar,
            @JsonProperty("sporsmal") String sporsmal,
            @JsonProperty("advarsel") String advarsel,
            @JsonProperty("underelementer") List<Element> underelementer
    ) {
        super(sporsmal, advarsel, underelementer);
        this.svar = svar;
    }

    public static VedleggElement nyttSvar(String sporsmal, List<String> svar) {
        return new VedleggElement(svar, sporsmal, null, null);
    }

    @JsonProperty("svar")
    public List<String> svar;

    @Override
    public String toString() {
        return "SvarElement{" +
                "sporsmal='" + sporsmal + '\'' +
                ", svar='" + svar.toString() + '\'' +
                ", underelementer=" + underelementer +
                '}';
    }
}
