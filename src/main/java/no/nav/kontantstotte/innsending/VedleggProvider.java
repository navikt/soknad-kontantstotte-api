package no.nav.kontantstotte.innsending;

import no.nav.familie.ks.kontrakter.søknad.Barn;
import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.kontantstotte.dokument.DokumentService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

class VedleggProvider {

    private final DokumentService dokumentService;

    VedleggProvider(DokumentService dokumentService) {
        this.dokumentService = dokumentService;
    }

    List<VedleggDto> hentVedleggFor(Soknad soknad) {
        List<List<VedleggMetadata>> alleVedlegg = Arrays.asList(
                soknad.barnehageplass.getRelevanteVedlegg()
        );

        return alleVedlegg.stream()
                          .flatMap(List::stream)
                          .map(v -> {
                              byte[] dokument = dokumentService.hentDokument(v.getFilreferanse());
                              if (dokument == null) {
                                  throw new InnsendingException(
                                          "Forsøker å sende inn en søknad med vedlegg som ikke finnes " + v.getFilreferanse());
                              }
                              return new VedleggDto(
                                      dokument,
                                      v.getFilnavn());
                          }).collect(toList());
    }

    List<VedleggDto> hentVedleggForNy(Søknad søknad) {
        List<String> alleVedlegg = søknad.getOppgittFamilieforhold().getBarna().stream()
                                         .filter(b -> b.getBarnehageVedlegg() != null)
                                         .map(Barn::getBarnehageVedlegg)
                                         .findAny()
                                         .orElse(Collections.emptyList());

        List<VedleggMetadata> alleVedleggMetadata = alleVedlegg.stream()
                                                               .map(vedlegg -> new VedleggMetadata(vedlegg,
                                                                                                   "Vedlegg: Barnehageplass"))
                                                               .collect(toList());

        return alleVedleggMetadata.stream()
                                  .map(v -> {
                                      byte[] dokument = dokumentService.hentDokument(v.getFilreferanse());
                                      if (dokument == null) {
                                          throw new InnsendingException(
                                                  "Forsøker å sende inn en søknad med vedlegg som ikke finnes " +
                                                  v.getFilreferanse());
                                      }
                                      return new VedleggDto(
                                              dokument,
                                              v.getFilnavn());
                                  })
                                  .collect(toList());
    }
}
