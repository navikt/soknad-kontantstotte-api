package no.nav.kontantstotte.oppsummering.innsending.v2;

import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.OppsummeringGenerator;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadOppsummering;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering;
import no.nav.kontantstotte.tekst.TekstProvider;

import java.util.Map;

class NodeOppsummeringGenerator implements OppsummeringGenerator {
    private final PdfGenService pdfService;
    private final HtmlOppsummeringService htmlOppsummeringService;
    private final SoknadTilOppsummering soknadTilOppsummering;
    private final TekstProvider tekstProvider;

    public NodeOppsummeringGenerator(TekstProvider tekstProvider, HtmlOppsummeringService htmlOppsummeringService, PdfGenService pdfService, SoknadTilOppsummering soknadTilOppsummering) {
        this.tekstProvider = tekstProvider;
        this.htmlOppsummeringService = htmlOppsummeringService;
        this.pdfService = pdfService;
        this.soknadTilOppsummering = soknadTilOppsummering;
    }

    @Override
    public byte[] genererOppsummering(Soknad soknad, String fnr) {
        Map<String, String> tekster = tekstProvider.hentTekster(soknad.sprak);
        SoknadOppsummering oppsummering = soknadTilOppsummering.map(soknad, tekster, fnr);
        byte[] htmlBytes = htmlOppsummeringService.genererHtml(oppsummering);
        return pdfService.genererPdf(htmlBytes);
    }
}
