package no.nav.kontantstotte.innsending.oppsummering;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import java.net.URI;

class PdfConverter {


    private URI pdfSvgSupportGeneratorUrl;

    private final Client client;


    public PdfConverter(Client client, URI pdfSvgSupportGeneratorUrl) {
        this.client = client;
        this.pdfSvgSupportGeneratorUrl = pdfSvgSupportGeneratorUrl;
    }

    byte[] genererPdf(byte[] bytes) {

        return client
                .target(pdfSvgSupportGeneratorUrl)
                .path("v1/genpdf/html/kontantstotte")
                .request()
                .buildPost(Entity.entity(bytes, "text/html; charset=utf-8"))
                .invoke()
                .readEntity(byte[].class);
    }

}

