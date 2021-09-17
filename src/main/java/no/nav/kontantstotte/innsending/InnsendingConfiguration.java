package no.nav.kontantstotte.innsending;


import no.nav.kontantstotte.dokument.DokumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.kontantstotte.client.RestClientConfiguration;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringConfiguration;

@Configuration
@Import({RestClientConfiguration.class,
        OppsummeringConfiguration.class
})
public class InnsendingConfiguration {

    @Bean
    public VedleggProvider vedleggProvider(@Autowired DokumentService dokumentService) {
        return new VedleggProvider(dokumentService);
    }

}
