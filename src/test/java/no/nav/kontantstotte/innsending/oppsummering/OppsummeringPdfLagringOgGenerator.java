package no.nav.kontantstotte.innsending.oppsummering;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.OppsummeringHtmlGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OppsummeringPdfLagringOgGenerator extends OppsummeringPdfGenerator {
    public OppsummeringPdfLagringOgGenerator(OppsummeringHtmlGenerator oppsummeringHtmlGenerator, PdfConverter pdfConverter) {
        super(oppsummeringHtmlGenerator, pdfConverter);
    }

    @Override
    public byte[] generer(Soknad soknad, String fnr) {
        byte[] htmlBytes = super.generer(soknad, fnr);
        skrivTilFil(htmlBytes);
        return htmlBytes;
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
