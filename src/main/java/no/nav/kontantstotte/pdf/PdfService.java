package no.nav.kontantstotte.pdf;

import no.nav.kontantstotte.innsending.Soknad;

public class PdfService {

    React react;

    public PdfService() {
        this.react = new React();
    }

    public String genererHtmlForPdf(Soknad soknad) {

        String html = react.renderHTMLForPdf(soknad);
        //gjør kall til soknad-pdf-generator for å hente pdf

        /*
        //TODO sett opp authorization header

        WebTarget target = ClientBuilder.newClient().target("http://localhost:8080/");

        Response response = target
                .path("api/convert")
                .request()
                .buildPost(Entity.entity(html, MediaType.TEXT_HTML)).invoke();

        */
        return html;
    }
}
