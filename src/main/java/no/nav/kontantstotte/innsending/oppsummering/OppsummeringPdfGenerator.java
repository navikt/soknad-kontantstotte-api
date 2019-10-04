package no.nav.kontantstotte.innsending.oppsummering;

import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.OppsummeringHtmlGenerator;

public class OppsummeringPdfGenerator {
    private final PdfConverter pdfConverter;
    private final OppsummeringHtmlGenerator oppsummeringHtmlGenerator;

    public OppsummeringPdfGenerator(OppsummeringHtmlGenerator oppsummeringHtmlGenerator, PdfConverter pdfConverter) {
        this.oppsummeringHtmlGenerator = oppsummeringHtmlGenerator;
        this.pdfConverter = pdfConverter;
    }

    public byte[] generer(Soknad soknad, String fnr) {
        byte[] htmlBytes = oppsummeringHtmlGenerator.genererHtml(soknad, fnr);
        return pdfConverter.genererPdf(htmlBytes);
    }
    public byte[] genererNy(Søknad søknad, String fnr) {
        byte[] htmlBytes = oppsummeringHtmlGenerator.genererHtmlNy(søknad, fnr);
        System.out.println("HTML:\n" + new String(htmlBytes));
        return pdfConverter.genererPdf(htmlBytes);
    }
}
