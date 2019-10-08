package no.nav.kontantstotte.innsending.oppsummering;

import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.OppsummeringHtmlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OppsummeringPdfGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(OppsummeringPdfGenerator.class);
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
        LOG.info("HTML: {}", new String(htmlBytes));
        return pdfConverter.genererPdf(htmlBytes);
    }
}
