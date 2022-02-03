package no.nav.kontantstotte.innsyn.pdl.domene;

import java.util.List;
import java.util.stream.Collectors;

public class PdlHentPersonBolkResponse {

    private PdlHentPersoner data;
    private List<PdlError> errors;

    PdlHentPersonBolkResponse() {
        // for jackson mapping
    }

    public PdlHentPersonBolkResponse(PdlHentPersoner data, List<PdlError> errors) {
        this.data = data;
        this.errors = errors;
    }

    public PdlHentPersoner getData() {
        return data;
    }

    public List<PdlError> getErrors() {
        return errors;
    }

    public boolean harFeil() {
        return errors != null && !errors.isEmpty();
    }

    public String errorMessages() {
        if (errors != null) {
            return errors.stream().map(PdlError::getMessage).collect(Collectors.joining());
        }
        return "";
    }
}

