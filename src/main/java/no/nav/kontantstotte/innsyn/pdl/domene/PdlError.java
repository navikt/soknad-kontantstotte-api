package no.nav.kontantstotte.innsyn.pdl.domene;

import java.util.List;

public class PdlError {

    private String message;
    private List<PdlExtensions> pdlExtensions;

    public PdlError(String message, List<PdlExtensions> pdlExtensions) {
        this.message = message;
        this.pdlExtensions = pdlExtensions;
    }

    public String getMessage() {
        return message;
    }

    public List<PdlExtensions> getPdlExtensions() {
        return pdlExtensions;
    }
}
