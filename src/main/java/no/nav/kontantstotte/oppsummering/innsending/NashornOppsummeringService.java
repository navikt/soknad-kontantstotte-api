package no.nav.kontantstotte.oppsummering.innsending;

import no.nav.kontantstotte.oppsummering.Soknad;

class NashornOppsummeringService implements OppsummeringService {


    private final PdfService pdfService;

    private final OppsummeringTransformer oppsummeringTransformer;

    public NashornOppsummeringService(PdfService pdfService, OppsummeringTransformer oppsummeringTransformer) {
        this.pdfService = pdfService;
        this.oppsummeringTransformer = oppsummeringTransformer;
    }

    @Override
    public byte[] genererOppsummering(Soknad soknad) {
        String oppsummeringHtml = oppsummeringTransformer.renderHTMLForPdf(soknad);

        return pdfService.genererPdf(oppsummeringHtml);


    }
}
