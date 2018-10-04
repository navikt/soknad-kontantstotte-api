package no.nav.kontantstotte.oppsummering.innsending.v2;

import no.nav.kontantstotte.api.rest.TeksterResource;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.OppsummeringGenerator;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadOppsummering;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering;

import java.util.Map;

class NodeOppsummeringGenerator implements OppsummeringGenerator {
    private final PdfGenService pdfService;
    private final HtmlOppsummeringService htmlOppsummeringService;

    public NodeOppsummeringGenerator(HtmlOppsummeringService htmlOppsummeringService, PdfGenService pdfService) {
        this.htmlOppsummeringService = htmlOppsummeringService;
        this.pdfService = pdfService;
    }

    @Override
    public byte[] genererOppsummering(Soknad soknad) {
        Map<String, String> tekster = new TeksterResource().tekster(soknad.sprak);
        SoknadOppsummering oppsummering = new SoknadTilOppsummering().map(soknad, tekster);
        byte[] htmlBytes = htmlOppsummeringService.genererHtml(oppsummering);
        return pdfService.genererPdf(htmlBytes);
    }
}
