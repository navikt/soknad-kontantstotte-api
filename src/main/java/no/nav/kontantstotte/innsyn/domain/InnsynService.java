package no.nav.kontantstotte.innsyn.domain;

import java.util.List;

public interface InnsynService {
    Person hentPersonInfo(String fnr);
    List<Barn> hentBarnInfo(String fnr);
    void ping();
}
