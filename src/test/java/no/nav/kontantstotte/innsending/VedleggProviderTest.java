package no.nav.kontantstotte.innsending;

import no.nav.kontantstotte.SetOidcClaimsWithSubject;
import no.nav.kontantstotte.innsending.steg.Barnehageplass;
import no.nav.kontantstotte.storage.Storage;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VedleggProviderTest {

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

        soknad.barnehageplass.barnBarnehageplassStatus = Barnehageplass.BarnehageplassVerdier.skalSlutteIBarnehage;

        soknad.barnehageplass.skalSlutteIBarnehageVedlegg = Arrays.asList(
                new VedleggMetadata("1234-1234-1234", "fil1.pdf"),
                new VedleggMetadata("5678-5678-5678", "fil2.pdf")
        );

        when(storage.get(any(), any())).thenReturn(Optional.of("test".getBytes()));

        VedleggProvider vedleggProvider = new VedleggProvider(storage);

        List<VedleggDto> vedlegg = vedleggProvider.hentVedleggFor(soknad);

        assertThat(vedlegg).hasSize(2);
    }

    @Test
    public void at_bare_relevante_vedlegg_sendes() {

        Soknad soknad = new Soknad();

        soknad.barnehageplass.barnBarnehageplassStatus = Barnehageplass.BarnehageplassVerdier.harBarnehageplass;

        soknad.barnehageplass.skalSlutteIBarnehageVedlegg = Arrays.asList(
                new VedleggMetadata("1234-1234-1234", "fil1.pdf"),
                new VedleggMetadata("5678-5678-5678", "fil2.pdf")
        );

        when(storage.get(any(), any())).thenReturn(Optional.of("test".getBytes()));

        VedleggProvider vedleggProvider = new VedleggProvider(storage);

        List<VedleggDto> vedlegg = vedleggProvider.hentVedleggFor(soknad);

        assertThat(vedlegg).isEmpty();
    }
}
