package no.nav.kontantstotte.innsending;

import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.kontantstotte.client.RestClientConfigration;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringConfiguration;
import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.s3.S3StorageConfiguration;

@Configuration
@Import({RestClientConfigration.class,
        OppsummeringConfiguration.class,
        S3StorageConfiguration.class
})
public class InnsendingConfiguration {

    @Bean
    public VedleggProvider vedleggProvider(@Named("encryptedStorage") Storage storage) {
        return new VedleggProvider(storage);
    }

}
