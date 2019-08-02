package no.nav.kontantstotte.innsending;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InnsendingException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(InnsendingException.class);

    public InnsendingException(String message) {
        super(message);
        log.info(message);
    }

    public InnsendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
