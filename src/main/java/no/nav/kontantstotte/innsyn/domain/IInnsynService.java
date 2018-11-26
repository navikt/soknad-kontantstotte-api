package no.nav.kontantstotte.innsyn.domain;

import java.io.IOException;
import java.util.List;

public interface IInnsynService {
    Person hentPersonInfo(String fnr);
    List<Barn> hentBarnInfo(String fnr);
}
