package no.nav.kontantstotte.innsyn.pdl.domene;

import java.util.List;
import java.util.stream.Collectors;

public class PdlHentPersonResponse<T> {

    private T data;
    private List<PdlError> errors;

    PdlHentPersonResponse() {
        // for jackson mapping
    }

    public PdlHentPersonResponse(T data, List<PdlError> errors) {
        this.data = data;
        this.errors = errors;
    }

    public T getData() {
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

