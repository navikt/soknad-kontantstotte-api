package no.nav.kontantstotte.innsending.oppsummering;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.HtmlConverter;
import no.nav.kontantstotte.innsending.oppsummering.html.SoknadOppsummering;
import no.nav.kontantstotte.innsending.oppsummering.html.SoknadTilOppsummering;
import no.nav.kontantstotte.tekst.TekstProvider;

import java.util.Map;

public class OppsummeringPdfGenerator {
    private final PdfConverter pdfConverter;
    private final HtmlConverter htmlConverter;
    private final SoknadTilOppsummering soknadTilOppsummering;
    private final TekstProvider tekstProvider;

    public OppsummeringPdfGenerator(TekstProvider tekstProvider, HtmlConverter htmlConverter, PdfConverter pdfConverter, SoknadTilOppsummering soknadTilOppsummering) {
        this.tekstProvider = tekstProvider;
        this.htmlConverter = htmlConverter;
        this.pdfConverter = pdfConverter;
        this.soknadTilOppsummering = soknadTilOppsummering;
    }

    public byte[] generer(Soknad soknad, String fnr) {
        Map<String, String> tekster = tekstProvider.hentTekster(soknad.sprak);
        SoknadOppsummering oppsummering = soknadTilOppsummering.map(soknad, tekster, fnr);
        byte[] htmlBytes = htmlConverter.genererHtml(oppsummering);
        return pdfConverter.genererPdf(htmlBytes);
    }
}
