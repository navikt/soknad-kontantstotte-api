package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.tekst.TekstService;
import org.junit.Test;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TekstnokkelTest {


    private TekstService tekstProvider = new TekstService();

    @Test

    public void nokler_definert_i_enum_skal_finnes_for_alle_sprak() {
        Stream.of(TekstService.DEFAULT_VALID_LANGUAGES)
                .forEach(s -> {
                    Map<String, String> tekster = tekstProvider.hentTekster(s);
                    assertThat(tekster.keySet())
                            .as("Nokler definert som Tekstnokkel-enum skal finnes i tekstbundle for sprak: %s", s)
                            .containsAll(() ->
                                    Stream.of(Tekstnokkel.values()).map(Tekstnokkel::getNokkel).iterator());

                });
    }
}
