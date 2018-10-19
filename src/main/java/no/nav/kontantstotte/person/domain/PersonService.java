package no.nav.kontantstotte.person.domain;

public interface PersonService {
    Person hentPersonInfo(String fnr) throws PersonServiceException;

    void ping();
}
