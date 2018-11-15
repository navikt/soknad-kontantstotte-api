package no.nav.kontantstotte.innsending.oppsummering;

import no.nav.kontantstotte.innsending.InnsendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

class PdfConverter {

    private static final Logger log = LoggerFactory.getLogger(PdfConverter.class);

    private URI pdfSvgSupportGeneratorUrl;

    private final Client client;


    public PdfConverter(Client client, URI pdfSvgSupportGeneratorUrl) {
        this.client = client;
        this.pdfSvgSupportGeneratorUrl = pdfSvgSupportGeneratorUrl;
    }

    byte[] genererPdf(byte[] bytes) {

        Response response = client
                .target(pdfSvgSupportGeneratorUrl)
                .path("v1/genpdf/html/kontantstotte")
                .request()
                .buildPost(Entity.entity(bytes, "text/html; charset=utf-8"))
                .invoke();

        if(!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            throw new InnsendingException("Response fra pdf-generator: "+ response.getStatus() + ". Response.entity: " + response.readEntity(String.class));
        }

        log.info("Konvertert søknad til pdf");

        return response.readEntity(byte[].class);
    }

}

