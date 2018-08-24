package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.pdf.PdfService;
import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestPart;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static java.time.LocalDateTime.now;

@Component
@Path("sendinn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class InnsendingResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Soknad sendInnSoknad(@FormDataParam("soknad") Soknad soknad) {
        soknad.innsendingTimestamp = now();

        PdfService pdfService = new PdfService();
        String html = pdfService.genererHtmlForPdf(soknad);

        WebTarget target = ClientBuilder.newClient()
                .register(OidcClientRequestFilter.class)
                .target("http://localhost:8081/");

        Response response = target.path("api/convert")
                .request()
                .buildPost(Entity.entity(html, MediaType.TEXT_HTML))
                .invoke();

        System.out.println("Response status PDF-kall:" + response.getStatus());

        // TODO: Send pdf videre til proxy i stedet for å skrive til fil

        try {
            new File("/Users/martineenger/nav/soknad-kontantstotte-api/TEST.pdf");
            OutputStream out = new FileOutputStream("/Users/martineenger/nav/soknad-kontantstotte-api/TEST.pdf");
            byte[] in = response.readEntity(byte[].class);
            out.write(in);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return soknad;
    }
}
