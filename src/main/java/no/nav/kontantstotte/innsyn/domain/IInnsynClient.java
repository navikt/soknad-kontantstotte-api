package no.nav.kontantstotte.innsyn.domain;

import javax.ws.rs.core.Response;

public interface IInnsynClient {
    Response getInnsynResponse(String path, String fnr);
    void ping();
}
