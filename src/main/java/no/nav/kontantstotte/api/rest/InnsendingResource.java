package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.SoknadDto;
import no.nav.kontantstotte.pdf.PdfService;
import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Value;
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
import java.net.URI;

import static java.time.LocalDateTime.now;

@Component
@Path("sendinn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class InnsendingResource {
    @Value("${apikeys.key:x-nav-apiKey}")
    private String key;

    @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}")
    private URI proxyServiceUri;

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}")
    private String proxyApiKey;

    @Value("${SOKNAD_PDF_GENERATOR_URL}")
    private URI pdfGeneratorServiceUri;


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Soknad sendInnSoknad(@FormDataParam("soknad") Soknad soknad) {
        soknad.innsendingTimestamp = now();

        PdfService pdfService = new PdfService();
        String htmlSoknad = pdfService.genererHtmlForPdf(soknad);

        WebTarget target = ClientBuilder.newClient()
                .register(OidcClientRequestFilter.class)
                .target(pdfGeneratorServiceUri);

        Response response = target.path("convert")
                .request()
                .buildPost(Entity.entity(htmlSoknad, MediaType.TEXT_HTML))
                .invoke();

        System.out.println("Response status PDF-kall:" + response.getStatus());

        boolean skrivTilFil = true;
        try {
            byte[] byteSoknad = response.readEntity(byte[].class);

            // Benytter dummy fnr til vi får på plass fnr integrasjon.
            SoknadDto soknadDto = new SoknadDto("10108000398", byteSoknad);

            WebTarget sendSoknadPdf = ClientBuilder.newClient()
                    .register(OidcClientRequestFilter.class)
                    .target(proxyServiceUri);

            Response sendSoknadReq = sendSoknadPdf.path("soknad")
                    .request()
                    .header(key, proxyApiKey)
                    .buildPost(Entity.entity(soknadDto, MediaType.APPLICATION_JSON))
                    .invoke();

            System.out.println("Response status send PDF:" + sendSoknadReq.getStatus() + ", " + sendSoknadReq.getLocation() + ", " + sendSoknadReq);


            // Ved deploy må filskriving deaktiveres
            if (skrivTilFil) {
                new File("/Users/henninghakonsen/nav/soknad-kontantstotte-api/TEST.pdf");
                OutputStream out = new FileOutputStream("/Users/henninghakonsen/nav/soknad-kontantstotte-api/TEST.pdf");

                out.write(byteSoknad);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return soknad;
    }
}
