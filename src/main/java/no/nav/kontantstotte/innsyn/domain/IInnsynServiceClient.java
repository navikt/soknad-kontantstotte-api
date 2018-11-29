package no.nav.kontantstotte.innsyn.domain;

import javax.ws.rs.core.Response;
import java.util.List;

public interface IInnsynServiceClient {
    Person hentPersonInfo(String fnr);
    List<Barn> hentBarnInfo(String fnr);
    Response getInnsynResponse(String path, String fnr);
    void ping();
}
