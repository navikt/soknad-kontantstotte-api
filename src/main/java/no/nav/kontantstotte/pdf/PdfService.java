package no.nav.kontantstotte.pdf;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.SoknadDto;
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
    React react;

    @Value("${apikeys.key:x-nav-apiKey}")
    private String key;

    @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}")
    private URI proxyServiceUri;

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}")
    private String proxyApiKey;

    @Value("${SOKNAD_PDF_GENERATOR_URL}")
    private URI pdfGeneratorServiceUri;

    public PdfService() {
        this.react = new React();
    }

    private String genererHtmlForPdf(Soknad soknad) {
        return react.renderHTMLForPdf(soknad);
    }

    public Soknad genererOgSendPdf(Soknad soknad) {
        PdfService pdfService = new PdfService();
        String htmlSoknad = pdfService.genererHtmlForPdf(soknad);

        WebTarget target = ClientBuilder.newClient()
                .register(OidcClientRequestFilter.class)
                .target(pdfGeneratorServiceUri);

        Response response = target.path("convert")
                .request()
                .buildPost(Entity.entity(htmlSoknad, MediaType.TEXT_HTML))
                .invoke();

        byte[] byteSoknad = response.readEntity(byte[].class);

        // Benytter dummy fnr til vi får på plass fnr integrasjon.
        SoknadDto soknadDto = new SoknadDto("10108000398", byteSoknad);

        WebTarget sendSoknadPdf = ClientBuilder.newClient()
                .register(OidcClientRequestFilter.class)
                .target(proxyServiceUri);

        sendSoknadPdf.path("soknad")
                .request()
                .header(key, proxyApiKey)
                .buildPost(Entity.entity(soknadDto, MediaType.APPLICATION_JSON))
                .invoke();

        return soknad;
    }

    private void skrivTilFil(byte[] soknad) {
        try {
            new File("/tmp/soknad-kontantstotte-api/TEST.pdf");
            OutputStream out = new FileOutputStream("/tmp/TEST.pdf");
            out.write(soknad);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
