package no.nav.kontantstotte.innsending;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InnsendingException extends RuntimeException {

    public InnsendingException(String message) {
        super(message);
    }

    public InnsendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
