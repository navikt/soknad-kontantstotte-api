package no.nav.kontantstotte.innsending.oppsummering.html;

import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.SøknadOppsummering;

public class OppsummeringHtmlGenerator {

    private final SoknadTilOppsummering soknadTilOppsummering;
    private final HtmlConverter htmlConverter;


    public OppsummeringHtmlGenerator(SoknadTilOppsummering soknadTilOppsummering, HtmlConverter htmlConverter) {
        this.soknadTilOppsummering = soknadTilOppsummering;
        this.htmlConverter = htmlConverter;
    }

    public byte[] genererHtml(Soknad soknad, String fnr) {
        SoknadOppsummering oppsummering = soknadTilOppsummering.map(soknad, fnr);
        return htmlConverter.genererHtml(oppsummering);
    }

    public byte[] genererHtmlNy(Søknad søknad, String fnr) {
        SøknadOppsummering oppsummering = soknadTilOppsummering.map(søknad, fnr);
        return htmlConverter.genererHtml(oppsummering);
    }
}

