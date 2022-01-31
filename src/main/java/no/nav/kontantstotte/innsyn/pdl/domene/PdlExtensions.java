package no.nav.kontantstotte.innsyn.pdl.domene;

public class PdlExtensions {

    private String code;

    PdlExtensions() {
        // for Jackson mapping
    }

    public PdlExtensions(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
