package no.nav.kontantstotte.person.domain;

public class SikkerhetsbegrensningException extends RuntimeException {
    public SikkerhetsbegrensningException(String msg) {
        super(msg);
    }
}
