package no.nav.kontantstotte.service;

import no.finn.unleash.Unleash;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class PdfService {
    public static final String BRUK_PDFGEN = "kontantstotte.pdfgen";

    private URI pdfGeneratorServiceUri;
    private URI pdfgenServiceUri;

    private final Client client;

    @Autowired
    private Unleash unleash;

    public PdfService(Client client, URI pdfGeneratorServiceUri, URI pdfgenServiceUri) {
        this.client = client;
        this.pdfGeneratorServiceUri = pdfGeneratorServiceUri;
        this.pdfgenServiceUri = pdfgenServiceUri;
    }

    public byte[] genererPdf(String oppsummeringHtml) {
        Response response;
        if (unleash.isEnabled(BRUK_PDFGEN)) {
            response = client
                    .target(pdfgenServiceUri)
                    .path("v1/genpdf/html/kontantstotte")
                    .request()
                    .buildPost(Entity.entity(oppsummeringHtml, "text/html; charset=utf-8"))
                    .invoke();
        } else {
            response = client
                    .target(pdfGeneratorServiceUri)
                    .path("convert")
                    .request()
                    .buildPost(Entity.entity(oppsummeringHtml, MediaType.TEXT_HTML_TYPE))
                    .invoke();
        }

        response.readEntity(byte[].class);

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
