package no.nav.kontantstotte.person.domain;

public class PersonServiceException extends Throwable {
    public PersonServiceException(Exception e) {
        super(e);
    }
}
