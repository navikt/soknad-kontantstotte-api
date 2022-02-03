package no.nav.kontantstotte.innsyn.pdl.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PdlForelderBarnRelasjon {

    private String relatertPersonsIdent;
    private String relatertPersonsRolle;

    PdlForelderBarnRelasjon() {
        // for jackson mapping
    }

    public PdlForelderBarnRelasjon(String relatertPersonsIdent, String relatertPersonsRolle){
        this.relatertPersonsIdent = relatertPersonsIdent;
        this.relatertPersonsRolle = relatertPersonsRolle;
    }

    public String getRelatertPersonsIdent() {
        return relatertPersonsIdent;
    }

    public String getRelatertPersonsRolle() {
        return relatertPersonsRolle;
    }
}
