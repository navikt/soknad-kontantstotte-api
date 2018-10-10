package no.nav.kontantstotte.oppsummering.innsending.v1;

import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.OppsummeringGenerator;

class NashornOppsummeringGenerator implements OppsummeringGenerator {


    private final PdfService pdfService;

    private final OppsummeringTransformer oppsummeringTransformer;

    public NashornOppsummeringGenerator(PdfService pdfService, OppsummeringTransformer oppsummeringTransformer) {
        this.pdfService = pdfService;
        this.oppsummeringTransformer = oppsummeringTransformer;
    }

    @Override
    public byte[] genererOppsummering(Soknad soknad, String fnr) {
        String oppsummeringHtml = oppsummeringTransformer.renderHTMLForPdf(soknad);

        return pdfService.genererPdf(oppsummeringHtml);


    }
}
