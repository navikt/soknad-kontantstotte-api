package no.nav.kontantstotte.innsyn.pdl.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PdlNavn {

    private String fornavn;
    private String mellomnavn;
    private String etternavn;

    PdlNavn() {
        // for Jackson mapping
    }

    public PdlNavn(String fornavn, String mellomnavn, String etternavn) {
        Objects.requireNonNull(fornavn);
        Objects.requireNonNull(etternavn);

        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getMellomnavn() {
        return mellomnavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public String fulltNavn() {
        if (getMellomnavn() == null) {
            return String.format("%s %s", getFornavn(), getEtternavn());
        } else {
            return String.format("%s %s %s", getFornavn(), getMellomnavn(), getEtternavn());
        }
    }
}
