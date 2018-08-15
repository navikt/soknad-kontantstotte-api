package no.nav.kontantstotte.pdf;

public class PdfService {

    React react;

    public PdfService() {
        this.react = new React();
    }

    public void genererPdf() {
        String html = react.renderHTMLForPdf();
        //gjør kall til soknad-pdf-generator for å hente pdf
    }
}
