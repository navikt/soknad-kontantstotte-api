package no.nav.kontantstotte.innsending.oppsummering.html;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.service.rest.InnsynRestConfiguration;
import no.nav.kontantstotte.tekst.TekstService;

@Import(InnsynRestConfiguration.class)
@Configuration
public class OppsummeringHtmlConfiguration {

    @Bean
    public SoknadTilOppsummering soknadTilOppsummering(InnsynService innsynServiceClient) {
        return new SoknadTilOppsummering(new TekstService(), innsynServiceClient);
    }

    @Bean
    public OppsummeringHtmlGenerator htmlOppsummeringGenerator(
            SoknadTilOppsummering soknadTilOppsummering,
            HtmlConverter htmlConverter
    ) {
        return new OppsummeringHtmlGenerator(soknadTilOppsummering, htmlConverter);
    }
}
