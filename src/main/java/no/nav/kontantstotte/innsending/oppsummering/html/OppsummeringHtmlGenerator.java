package no.nav.kontantstotte.innsending.oppsummering.html;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.tekst.TekstProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;

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
}

