package no.nav.kontantstotte.oppsummering.innsending;

import no.nav.kontantstotte.oppsummering.Soknad;

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

class PdfService {
    public static final String BRUK_PDFGEN = "kontantstotte.pdfgen";

    @Autowired
    private Unleash unleash;

    private URI pdfGeneratorUri;
    private URI pdfSvgSupportGeneratorUrl;

    private final Client client;

    private final OppsummeringTransformer oppsummeringTransformer;

    public PdfService(Client client, URI pdfGeneratorUri, URI pdfSvgSupportGeneratorUrl, OppsummeringTransformer oppsummeringTransformer) {
        this.client = client;
        this.pdfGeneratorUri = pdfGeneratorUri;
        this.pdfSvgSupportGeneratorUrl = pdfSvgSupportGeneratorUrl;
        this.oppsummeringTransformer = oppsummeringTransformer;
    }

    public byte[] genererPdf(Soknad soknad) {

        String oppsummeringHtml = oppsummeringTransformer.renderHTMLForPdf(soknad);

        if (unleash.isEnabled(BRUK_PDFGEN)) {
            return client
                    .target(pdfSvgSupportGeneratorUrl)
                    .path("v1/genpdf/html/kontantstotte")
                    .request()
                    .buildPost(Entity.entity(oppsummeringHtml, "text/html; charset=utf-8"))
                    .invoke()
                    .readEntity(byte[].class);
        } else {
            return client
                    .target(pdfGeneratorUri)
                    .path("convert")
                    .request()
                    .buildPost(Entity.entity(oppsummeringHtml, MediaType.TEXT_HTML))
                    .invoke()
                    .readEntity(byte[].class);
        }


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
