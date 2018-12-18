package no.nav.kontantstotte.innsending;

import no.nav.kontantstotte.EnableFeatureToggle;
import no.nav.kontantstotte.SetOidcClaimsWithSubject;
import no.nav.kontantstotte.storage.Storage;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_VEDLEGG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VedleggProviderTest {

    @Rule
    public EnableFeatureToggle enableFeatureToggle = new EnableFeatureToggle(KONTANTSTOTTE_VEDLEGG);

    @Rule
    public SetOidcClaimsWithSubject setOidcClaimsWithSubject = new SetOidcClaimsWithSubject("DUMMY_FNR");

    private Storage storage = mock(Storage.class);

    @Test
    public void at_vedlegg_konverteres_ok_uten_vedlegg() {

        Soknad soknad = new Soknad();

        VedleggProvider vedleggProvider = new VedleggProvider(storage);

        List<VedleggDto> vedlegg = vedleggProvider.hentVedleggFor(soknad);

        assertThat(vedlegg).hasSize(0);
    }

    @Test
    public void at_vedlegg_konverteres_rett() {

        Soknad soknad = new Soknad();

        soknad.vedlegg = Arrays.asList(
                new VedleggMetadata("abc", "tittel1", "vedlegg1"),
                new VedleggMetadata("ced", "tittel2", "vedlegg2"));

        when(storage.get(any(), any())).thenReturn(Optional.of("test".getBytes()));

        VedleggProvider vedleggProvider = new VedleggProvider(storage);

        List<VedleggDto> vedlegg = vedleggProvider.hentVedleggFor(soknad);

        assertThat(vedlegg).hasSize(2);
    }
}
