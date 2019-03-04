package no.nav.kontantstotte.innsending.oppsummering.html;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SvarElement extends Element {

    @JsonCreator
    public SvarElement(
            @JsonProperty("svar") String svar,
            @JsonProperty("sporsmal") String sporsmal,
            @JsonProperty("advarsel") String advarsel,
            @JsonProperty("underelementer") List<Element> underelementer
    ) {
        super(sporsmal, advarsel, underelementer);
        this.svar = svar;
    }

    public static SvarElement nyttSvar(String svar) {
        return new SvarElement(svar, null, null, null);
    }

    public static SvarElement nyttSvar(String sporsmal, String svar) {
        return new SvarElement(svar, sporsmal, null, null);
    }

    public static SvarElement nyttSvar(String sporsmal, String svar, String advarsel) {
        return new SvarElement(svar, sporsmal, advarsel, null);
    }

    @JsonProperty("svar")
    public String svar;

    @Override
    public String toString() {
        return "SvarElement{" +
                "sporsmal='" + sporsmal + '\'' +
                ", svar='" + svar + '\'' +
                ", underelementer=" + underelementer +
                '}';
    }
}
