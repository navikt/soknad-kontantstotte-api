package no.nav.kontantstotte.service;

import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class PdfService {

    @Value("${SOKNAD_PDF_GENERATOR_URL}")
    private URI pdfGeneratorServiceUri;

    public byte[] genererPdf(String oppsummeringHtml) {

        WebTarget target = ClientBuilder.newClient()
                .register(OidcClientRequestFilter.class)
                .target(pdfGeneratorServiceUri);

        Response response = target.path("convert")
                .request()
                .buildPost(Entity.entity(oppsummeringHtml, MediaType.TEXT_HTML))
                .invoke();

        byte[] soknad = response.readEntity(byte[].class);

        return soknad;
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
