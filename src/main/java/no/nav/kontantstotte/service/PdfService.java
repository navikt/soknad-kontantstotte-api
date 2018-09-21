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
    private URI pdfgenServiceUri;

    private final Client client;

    public PdfService(Client client, URI pdfGeneratorServiceUri, URI pdfgenServiceUri) {
        this.client = client;
        this.pdfGeneratorServiceUri = pdfGeneratorServiceUri;
    }

    public byte[] genererPdf(String oppsummeringHtml) {

        // Når vi kan ta i bruk pdfgen må vi endre mediatype til "text/html; charset=utf-8"
        Response response = client
                .target(pdfGeneratorServiceUri)
                .path("convert")
                .request()
                .buildPost(Entity.entity(oppsummeringHtml, "text/html; charset=utf-8"))
                .invoke();

        skrivTilFil(response.readEntity(byte[].class));
        return null;

    }

    private void skrivTilFil(byte[] soknad) {
        try {
            new File(System.getProperty("user.dir") + "/TEST.pdf");
            OutputStream out = new FileOutputStream(System.getProperty("user.dir") + "/TEST.pdf");
            out.write(soknad);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
