package no.nav.kontantstotte.innsending.oppsummering.html;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel;
import no.nav.kontantstotte.tekst.TekstProvider;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SoknadTilOppsummeringTest {
    public static final String NEI = "Nei";

    @Test
    public void bolkerIRettRekkefølge() {
        String fnr = "XXXXXXXXXX";
        Soknad soknad = new Soknad();
        soknad.markerInnsendingsTidspunkt();

        TekstProvider mock = mock(TekstProvider.class);
        Map<String, String> tekster = tekster(
                tekst(KRAV_TIL_SOKER_TITTEL),
                tekst(BARN_TITTEL),
                tekst(BARNEHAGEPLASS_TITTEL),
                tekst(FAMILIEFORHOLD_TITTEL),
                tekst(TILKNYTNING_TIL_UTLAND_TITTEL),
                tekst(UTENLANDSKE_YTELSER_TITTEL),
                tekst(UTENLANDSK_KONTANTSTOTTE_TITTEL)
        );
        when(mock.hentTekster(any())).thenReturn(tekster);

        SoknadOppsummering oppsummering = new SoknadTilOppsummering(mock).map(
                soknad,
                fnr);

        assertThat(oppsummering.getBolker())
                .extracting("bolknavn", "tittel")
                .containsSequence(
                        tuple(null, KRAV_TIL_SOKER_TITTEL.getNokkel()),
                        tuple(null, BARN_TITTEL.getNokkel()),
                        tuple(null, BARNEHAGEPLASS_TITTEL.getNokkel()),
                        tuple(null, FAMILIEFORHOLD_TITTEL.getNokkel()),
                        tuple(null, TILKNYTNING_TIL_UTLAND_TITTEL.getNokkel()),
                        tuple("arbeidIUtlandet", null),
                        tuple(null, UTENLANDSKE_YTELSER_TITTEL.getNokkel()),
                        tuple(null, UTENLANDSK_KONTANTSTOTTE_TITTEL.getNokkel()),
                        tuple("oppsummering", null)
                );
        assertThat(oppsummering.getFnr()).isEqualTo(fnr);
    }

    private Map<String, String> tekster(AbstractMap.SimpleEntry<String, String>... tekst) {
        return Collections.unmodifiableMap(Stream.of(tekst)
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
    }

    private AbstractMap.SimpleEntry<String, String> tekst(Tekstnokkel nokkel) {
        return new AbstractMap.SimpleEntry<>(nokkel.getNokkel(), nokkel.getNokkel());
    }
}