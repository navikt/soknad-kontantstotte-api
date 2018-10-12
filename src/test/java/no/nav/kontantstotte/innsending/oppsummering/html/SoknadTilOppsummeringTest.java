package no.nav.kontantstotte.innsending.oppsummering.html;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.oppsummering.html.SoknadOppsummering;
import no.nav.kontantstotte.innsending.oppsummering.html.SoknadTilOppsummering;
import no.nav.kontantstotte.innsending.oppsummering.html.mapping.BarnMapping;
import no.nav.kontantstotte.innsending.oppsummering.html.mapping.BarnehageplassMapping;
import no.nav.kontantstotte.innsending.oppsummering.html.mapping.FamilieforholdMapping;
import no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel;
import no.nav.kontantstotte.innsending.steg.Barn;
import no.nav.kontantstotte.innsending.steg.Barnehageplass;
import no.nav.kontantstotte.innsending.steg.Familieforhold;
import no.nav.kontantstotte.tekst.TekstProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
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

    private Unleash unleash;

    @Before
    public void init() {
        this.unleash = new FakeUnleash();
    }

    @Test
    public void bolkerIRettRekkefølge() {
        String fnr = "XXXXXXXXXX";
        Soknad soknad = new Soknad();
        soknad.markerInnsendingsTidspunkt();

        TekstProvider mock = mock(TekstProvider.class);
        Map<String, String> tekster = tekster(
                tekst(BARN_TITTEL),
                tekst(BARNEHAGEPLASS_TITTEL),
                tekst(FAMILIEFORHOLD_TITTEL),
                tekst(UTENLANDSK_KONTANTSTOTTE_TITTEL)
        );
        when(mock.hentTekster(any())).thenReturn(tekster);

        SoknadOppsummering oppsummering = new SoknadTilOppsummering(mock, unleash).map(
                soknad,
                fnr);

        assertThat(oppsummering.getBolker())
                .extracting("bolknavn", "tittel")
                .containsSequence(
                        tuple("kravTilSoker", null),
                        tuple(null, BARN_TITTEL.getNokkel()),
                        tuple(null, BARNEHAGEPLASS_TITTEL.getNokkel()),
                        tuple(null, FAMILIEFORHOLD_TITTEL.getNokkel()),
                        tuple("tilknytningTilUtland", null),
                        tuple("arbeidIUtlandet", null),
                        tuple("utenlandskeYtelser", null),
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