package no.nav.kontantstotte.innsyn.service.rest;

import no.nav.kontantstotte.innsyn.domain.IInnsynClient;
import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import no.nav.log.MDCConstants;
import org.slf4j.MDC;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

class InnsynClient implements IInnsynClient {

    private static final String CONSUMER_ID = "soknad-kontantstotte-api";

    public final Client client;

    private URI tpsInnsynServiceUri;

    @Inject
    InnsynClient(Client client, URI tpsInnsynServiceUri) {
        this.client = client;
        this.tpsInnsynServiceUri = tpsInnsynServiceUri;
    }

    @Override
    public void ping() {
        Response response = client.target(tpsInnsynServiceUri)
                .path("/internal/alive")
                .request()
                .get();

        if(!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            throw new InnsynOppslagException("TPS innsyn service is not up");
        }
    }

    @Override
    public String toString() {
        return "InnsynService{" +
                "client=" + client +
                ", tpsInnsynServiceUri=" + tpsInnsynServiceUri +
                '}';
    }

    public Response getInnsynResponse(String path, String fnr) {
        Response response = client.target(tpsInnsynServiceUri)
                .path(path)
                .request()
                .header("Nav-Call-Id", MDC.get(MDCConstants.MDC_CORRELATION_ID))
                .header("Nav-Consumer-Id", CONSUMER_ID)
                .header("Nav-Personident", fnr)
                .get();

        if(!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            throw new InnsynOppslagException(response.readEntity(String.class));
        } else {
            return response;
        }
    }
}
