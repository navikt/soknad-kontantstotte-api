package no.nav.kontantstotte.oppsummering.innsending.v2;

import no.nav.kontantstotte.api.rest.TeksterResource;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.OppsummeringService;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadOppsummering;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering;
import no.nav.kontantstotte.tekst.TekstProvider;

import java.util.Map;

class NodeOppsummeringService implements OppsummeringService {


    private final PdfGenService pdfService;
    private final HtmlOppsummeringService htmlOppsummeringService;

    public NodeOppsummeringService(HtmlOppsummeringService htmlOppsummeringService, PdfGenService pdfService) {
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
