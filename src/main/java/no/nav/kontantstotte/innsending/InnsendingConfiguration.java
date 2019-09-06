package no.nav.kontantstotte.innsending;


import no.nav.kontantstotte.storage.encryption.EncryptedStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.kontantstotte.client.RestClientConfiguration;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringConfiguration;
import no.nav.kontantstotte.storage.s3.S3StorageConfiguration;

@Configuration
@Import({RestClientConfiguration.class,
        OppsummeringConfiguration.class,
        S3StorageConfiguration.class
})
public class InnsendingConfiguration {

    @Bean
    public VedleggProvider vedleggProvider(@Autowired EncryptedStorage storage) {
        return new VedleggProvider(storage);
    }

}
