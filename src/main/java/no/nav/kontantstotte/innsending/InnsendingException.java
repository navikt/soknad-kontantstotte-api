package no.nav.kontantstotte.innsending;

public class InnsendingException extends RuntimeException {

    public InnsendingException(String message) {
        super(message);
    }

    public InnsendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
