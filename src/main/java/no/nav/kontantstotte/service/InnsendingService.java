package no.nav.kontantstotte.service;

import no.nav.kontantstotte.oppsummering.SoknadDto;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class InnsendingService extends ProxyService {

    public Response sendInnSoknad(SoknadDto soknadDto) {

        Response innsendingResponse = proxyTarget().path("soknad")
                .request()
                .header(key, proxyApiKey)
                .buildPost(Entity.entity(soknadDto, MediaType.APPLICATION_JSON))
                .invoke();

        return innsendingResponse;
    }
}
