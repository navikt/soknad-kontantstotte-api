package no.nav.kontantstotte.oppsummering.innsending.v2;

import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.OppsummeringService;

class NodeOppsummeringService implements OppsummeringService {


    private final PdfGenService pdfService;

    public NodeOppsummeringService(PdfGenService pdfService) {
        this.pdfService = pdfService;
    }

    @Override
    public byte[] genererOppsummering(Soknad soknad) {
        byte[] bytes = soknad.toString().getBytes();
        return pdfService.genererPdf(bytes);


    }
}
