package no.nav.kontantstotte.innsyn.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class FortroligAdresseException extends RuntimeException {
    public FortroligAdresseException(String msg) {
        super(msg);
    }
}
