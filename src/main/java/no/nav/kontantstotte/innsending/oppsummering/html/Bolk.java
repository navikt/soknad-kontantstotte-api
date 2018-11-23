package no.nav.kontantstotte.innsending.oppsummering.html;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Bolk {

    @JsonProperty("bolknavn")
    public String bolknavn;

    @JsonProperty("tittel")
    public String tittel;

    @JsonProperty("undertittel")
    public String undertittel;

    @JsonProperty("elementer")
    public List<Element> elementer;

    @Override
    public String toString() {
        return "Bolk{" +
                "bolknavn='" + bolknavn + '\'' +
                ", tittel='" + tittel + '\'' +
                ", undertittel='" + undertittel + '\'' +
                ", antall elementer=" + elementer.size() +
                '}';
    }

    public Bolk add(Element element) {
        if(elementer == null) {
            elementer = new ArrayList<>();
        }

        elementer.add(element);
        return this;
    }

    public Bolk medTittel(String tittel) {
        this.tittel = tittel;
        return this;
    }

    public Bolk medUndertittel(String undertittel) {
        this.undertittel = undertittel;
        return this;
    }
}
