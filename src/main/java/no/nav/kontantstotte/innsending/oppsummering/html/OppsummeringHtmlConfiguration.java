package no.nav.kontantstotte.innsending.oppsummering.html;

import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.service.rest.InnsynRestConfiguration;
import no.nav.kontantstotte.tekst.TekstService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import java.net.URI;

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

    @Bean
    public HtmlConverter htmlOppsummeringService(
            @Named("client") Client client,
            @Value("${SOKNAD_HTML_GENERATOR_URL}") URI htmlGeneratorUrl
    ) {
        return new HtmlConverter(client, htmlGeneratorUrl);
    }

}
