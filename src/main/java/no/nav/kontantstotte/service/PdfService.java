package no.nav.kontantstotte.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class PdfService {

    private URI pdfGeneratorServiceUri;

    private final Client client;

    public PdfService(Client client, URI pdfGeneratorServiceUri) {
        this.client = client;
        this.pdfGeneratorServiceUri = pdfGeneratorServiceUri;
    }

    public byte[] genererPdf(String oppsummeringHtml) {

        Response response = client
                .target(pdfGeneratorServiceUri)
                .path("convert")
                .request()
                .buildPost(Entity.entity(oppsummeringHtml, MediaType.TEXT_HTML))
                .invoke();

        return response.readEntity(byte[].class);
    }

    private void skrivTilFil(byte[] soknad) {
        try {
            new File("/tmp/TEST.pdf");
            OutputStream out = new FileOutputStream("/tmp/TEST.pdf");
            out.write(soknad);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
