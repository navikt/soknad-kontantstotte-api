package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;

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
}
