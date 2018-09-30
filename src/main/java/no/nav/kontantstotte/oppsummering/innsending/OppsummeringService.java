package no.nav.kontantstotte.oppsummering.innsending;

import no.nav.kontantstotte.oppsummering.Soknad;

class OppsummeringService {


    private final PdfService pdfService;

    private final OppsummeringTransformer oppsummeringTransformer;

    public OppsummeringService(PdfService pdfService, OppsummeringTransformer oppsummeringTransformer) {
        this.pdfService = pdfService;
        this.oppsummeringTransformer = oppsummeringTransformer;
    }

    public byte[] genererOppsummering(Soknad soknad) {
        String oppsummeringHtml = oppsummeringTransformer.renderHTMLForPdf(soknad);

        return pdfService.genererPdf(oppsummeringHtml);


    }
}
