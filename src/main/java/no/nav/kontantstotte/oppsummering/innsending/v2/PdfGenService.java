package no.nav.kontantstotte.oppsummering.innsending.v2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import java.net.URI;

public class PdfGenService {


    private URI pdfSvgSupportGeneratorUrl;

    private final Client client;


    public PdfGenService(Client client, URI pdfSvgSupportGeneratorUrl) {
        this.client = client;
        this.pdfSvgSupportGeneratorUrl = pdfSvgSupportGeneratorUrl;
    }

    public byte[] genererPdf(byte[] bytes) {

        return client
                .target(pdfSvgSupportGeneratorUrl)
                .path("v1/genpdf/html/kontantstotte")
                .request()
                .buildPost(Entity.entity(bytes, "text/html; charset=utf-8"))
                .invoke()
                .readEntity(byte[].class);
    }

}

