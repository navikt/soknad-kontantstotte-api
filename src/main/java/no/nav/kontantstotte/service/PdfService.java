package no.nav.kontantstotte.service;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.api.rest.InnsendingResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(PdfService.class);
    public static final String BRUK_PDFGEN = "kontantstotte.pdfgen";
    public static final String BRUK_PDFGEN_LOCAL = "kontantstotte.pdfgen_local";

    private URI pdfGeneratorServiceUri;
    private URI pdfgenServiceUri;
    private URI pdfgenServiceUriLocal;

    private final Client client;

    @Autowired
    private Unleash unleash;

    public PdfService(Client client, URI pdfGeneratorServiceUri) {
        this.client = client;
        this.pdfGeneratorServiceUri = pdfGeneratorServiceUri;
        try {
            this.pdfgenServiceUri = new URI("http://pdf-gen.default/api");
            this.pdfgenServiceUriLocal = new URI("http://localhost:8082/api");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public byte[] genererPdf(String oppsummeringHtml) {
        logger.warn(pdfGeneratorServiceUri + ", " + pdfgenServiceUri);
        logger.warn(oppsummeringHtml.substring(0, 20));

        Response response;
        if (unleash.isEnabled(BRUK_PDFGEN_LOCAL)) {
            response = client
                    .target(pdfgenServiceUriLocal)
                    .path("v1/genpdf/html/kontantstotte")
                    .request()
                    .buildPost(Entity.entity(oppsummeringHtml, "text/html; charset=utf-8"))
                    .invoke();
        } else if (unleash.isEnabled(BRUK_PDFGEN)) {
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
                    .buildPost(Entity.entity(oppsummeringHtml, MediaType.TEXT_HTML))
                    .invoke();
        }

        logger.warn(response.toString());

        return response.readEntity(byte[].class);

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
