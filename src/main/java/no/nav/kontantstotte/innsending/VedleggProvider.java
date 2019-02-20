package no.nav.kontantstotte.innsending;

import no.nav.kontantstotte.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_VEDLEGG;
import static no.nav.kontantstotte.config.toggle.UnleashProvider.toggle;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

class VedleggProvider {

    private final Storage storage;

    VedleggProvider(Storage storage) {
        this.storage = storage;
    }

    List<VedleggDto> hentVedleggFor(Soknad soknad) {

        if(toggle(KONTANTSTOTTE_VEDLEGG).isDisabled()) {
            return null;
        }

        String directory = hentFnrFraToken();

        return soknad.vedlegg.stream()
                .map(v -> new VedleggDto(
                        storage.get(directory, v.getVedleggsId())
                                .orElseThrow(() -> new InnsendingException("Foresøker å sende inn en søknad med vedlegg som ikke finnes " + v.getVedleggsId())),
                        v.getTittel(),
                        v.getDokumenttype()))
                .collect(Collectors.toList());
    }
}
