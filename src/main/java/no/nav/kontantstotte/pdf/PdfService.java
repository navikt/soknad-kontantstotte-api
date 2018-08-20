package no.nav.kontantstotte.pdf;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PdfService {

    React react;

    public PdfService() {
        this.react = new React();
    }

    public void genererPdf() {

        String html = react.renderHTMLForPdf();
        //gjør kall til soknad-pdf-generator for å hente pdf
        System.out.println(html);
        /*
        String html = "<html>" +
                "<head>" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>" +
                "<style type=\"text/css\">\n" +
                "        @page:first { margin: 12mm 2cm 2.5cm; }\n" +
                "        *       { box-sizing: border-box; }\n" +
                "        body    { font-family: ArialSystem, sans-serif; font-size: 10pt; line-height: 1.4em; margin: 0; color: #3e3832; }\n" +
                "\n" +
                "        .typo-undertittel  { font-family: Modus, ArialSystem, sans-serif; font-size: 15pt; font-weight: bold; }\n" +
                "        .typo-element-regular  { font-family: ArialSystem, sans-serif; font-size: 10.5pt; font-weight: normal; }\n" +
                "        .typo-element  { font-family: ArialSystem, sans-serif; font-size: 10.5pt; font-weight: bold; }\n" +
                "\n" +
                "        .logo      { display: block; margin-left: 80mm; width: 20mm; }\n" +
                "        .hode      { text-align: center; margin: 5.2mm 0 10.5mm; padding-bottom: 8.4mm; border-bottom: .5pt solid #b7b1a9; }\n" +
                "        .hode hr   { border: none; border-top: .5pt solid #b7b1a9; width: 8mm; margin: 5.2mm auto 8.4mm; }\n" +
                "\n" +
                "        ul { list-style: none; padding-left: 0; margin-top: 0;}\n" +
                "        .innsendt li { background: url(img/hake_sort_200.jpg) no-repeat left center; padding-left: 4mm; background-size: 2.3mm; }\n" +
                "        .ikke-innsendt li { background: url(img/minus_sort.gif) no-repeat left center; padding-left: 4mm; background-size: 2mm; }\n" +
                "\n" +
                "        .informasjon {padding-bottom: 8.4mm; border-bottom: .5pt solid #b7b1a9; }\n" +
                "\n" +
                "        .blokk-xxs { margin-bottom: 2.1mm; }\n" +
                "        .blokk-s   { margin-bottom: 5.4mm; }\n" +
                "        .blokk-m   { margin-bottom: 8.4mm; }\n" +
                "        .blokk-l   { margin-bottom: 10.5mm; }\n" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "<div>" +
                "<h1>Søknad om kontantstøtte</h1>" +
                "</div>" +
                "</body>" +
                "</html>";
        */


        //TODO sett opp authorization header

        WebTarget target = ClientBuilder.newClient().target("http://localhost:8080/");

        Response response = target
                .path("api/convert")
                .request()
                .buildPost(Entity.entity(html, MediaType.TEXT_HTML)).invoke();

        /*
        try {
            File file = new File("/Users/martineenger/nav/soknad-kontantstotte-api/TEST.pdf");
            OutputStream out = new FileOutputStream("/Users/martineenger/nav/soknad-kontantstotte-api/TEST.pdf");
            byte[] in = response.readEntity(byte[].class);
            out.write(in);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        System.out.println(response.getStatus());
    }
}
