package no.nav.kontantstotte.innsending;

import no.nav.familie.ks.kontrakter.søknad.Barn;
import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.kontantstotte.storage.encryption.EncryptedStorage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

class VedleggProvider {

    private final EncryptedStorage storage;

    VedleggProvider(EncryptedStorage storage) {
        this.storage = storage;
    }

    List<VedleggDto> hentVedleggFor(Soknad soknad) {

        String directory = hentFnrFraToken();

        List<List<VedleggMetadata>> alleVedlegg = Arrays.asList(
                soknad.barnehageplass.getRelevanteVedlegg()
        );

        return alleVedlegg.stream()
                .flatMap(List::stream)
                .map(v -> new VedleggDto(
                        storage.get(directory, v.getFilreferanse())
                                .orElseThrow(() -> new InnsendingException("Forsøker å sende inn en søknad med vedlegg som ikke finnes " + v.getFilreferanse())),
                        v.getFilnavn()))
                .collect(toList());
    }

    List<VedleggDto> hentVedleggForNy(Søknad søknad) {
        String directory = hentFnrFraToken();

        List<String> alleVedlegg = søknad.getOppgittFamilieforhold().getBarna().stream()
                .filter(b -> b.getBarnehageVedlegg() != null)
                .map(Barn::getBarnehageVedlegg)
                .findAny()
                .orElse(Collections.emptyList());

        List<VedleggMetadata> alleVedleggMetadata = alleVedlegg.stream()
                .map(vedlegg -> new VedleggMetadata(vedlegg, "Vedlegg: Barnehageplass"))
                .collect(toList());

        return alleVedleggMetadata.stream()
                .map(v -> new VedleggDto(
                        storage.get(directory, v.getFilreferanse())
                                .orElseThrow(() -> new InnsendingException("Forsøker å sende inn en søknad med vedlegg som ikke finnes " + v.getFilreferanse())),
                        v.getFilnavn()))
                .collect(toList());
    }
}
