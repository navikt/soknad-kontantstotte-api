package no.nav.kontantstotte.innsyn.pdl.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PdlStatsborgerskap {

    private String land;

    PdlStatsborgerskap() {
        // for jackson mapping
    }

    public PdlStatsborgerskap(String land) {
        this.land = land;
    }

    public String getLand() {
        return land;
    }
}
