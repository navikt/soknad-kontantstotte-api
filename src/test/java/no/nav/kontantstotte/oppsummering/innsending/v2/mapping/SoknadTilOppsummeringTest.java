package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.api.TestLauncher;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barn;
import no.nav.kontantstotte.oppsummering.bolk.Barnehageplass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.BRUK_PDFGEN;
import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_NY_OPPSUMMERING;
import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static no.nav.kontantstotte.oppsummering.innsending.ArkivInnsendingService.hentFnrFraToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SoknadTilOppsummeringTest {
    private static String fnr = "MOCK_FNR";

    @Test
    public void bolkerIRettRekkefølge() {
        Map<String, String> tekster = mock(Map.class);
        when(tekster.get(any())).thenReturn("tekstinnhold");
        SoknadOppsummering oppsummering = new SoknadTilOppsummering().map(new Soknad(), tekster, fnr, new FakeUnleash());

        assertThat(oppsummering.getBolker())
                .extracting("bolknavn")
                .containsSequence(
                        "kravTilSoker",
                        null,
                        null,
                        "familieforhold", 
                        "tilknytningTilUtland", 
                        "arbeidIUtlandet", 
                        "utenlandskeYtelser", 
                        "utenlandskKontantstotte", 
                        "oppsummering"
                );
        assertThat(oppsummering.getFnr()).isEqualTo(fnr);
    }


    @Test
    public void tilBarneBolk() {
        String tittel = "BARN";
        String undertittel = "Barn du søker kontantstøtte for";
        String navn = "Navn";
        String fodselsdato = "Fødselsdato";

        Map<String, String> tekster = Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>(SoknadTilOppsummering.BARN_TITTEL, tittel),
                new AbstractMap.SimpleEntry<>(SoknadTilOppsummering.BARN_UNDERTITTEL, undertittel),
                new AbstractMap.SimpleEntry<>(SoknadTilOppsummering.BARN_NAVN, navn),
                new AbstractMap.SimpleEntry<>(SoknadTilOppsummering.BARN_FODSELSDATO, fodselsdato))
                .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));

        Barn innsendtBarn = new Barn();
        innsendtBarn.navn = "Barnets navn";
        innsendtBarn.fodselsdato = "01.01.2019";
        Bolk bolk = new SoknadTilOppsummering().mapBarn(innsendtBarn, tekster);
        assertThat(bolk)
                .extracting("tittel", "undertittel")
                .containsExactly(tittel, undertittel);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(navn, innsendtBarn.navn),
                        tuple(fodselsdato, innsendtBarn.fodselsdato));
    }

    @Test
    public void tilBarnehageplassBolk() {
        String tittel = "BARNEHAGEPLASS";
        String harBarnehageplass = "NEI";
        String barnBarnehageplassStatus = "garIkkeIBarnehage";
        String BARNEHAGEPLASS_GAR_IKKE_I_BARNEHAGE = "barnehageplass.garIkkeIBarnehage";

        Map<String, String> tekster = Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>(SoknadTilOppsummering.BARNEHAGEPLASS_TITTEL, tittel),
                new AbstractMap.SimpleEntry<>(SoknadTilOppsummering.HAR_BARNEHAGEPLASS, harBarnehageplass),
                new AbstractMap.SimpleEntry<>(SoknadTilOppsummering.BARN_BARNEHAGEPLASS_STATUS, barnBarnehageplassStatus),
                new AbstractMap.SimpleEntry<>(BARNEHAGEPLASS_GAR_IKKE_I_BARNEHAGE, barnBarnehageplassStatus))
                .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));

        Barnehageplass barnehageplass = new Barnehageplass();
        barnehageplass.harBarnehageplass = "NEI";
        barnehageplass.barnBarnehageplassStatus = Barnehageplass.BarnehageplassVerdier.garIkkeIBarnehage;

        Bolk bolk = new SoknadTilOppsummering().mapBarnehageplass(barnehageplass, tekster, new FakeUnleash());
        assertThat(bolk)
                .extracting("tittel")
                .containsExactly(tittel);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(harBarnehageplass, barnehageplass.harBarnehageplass),
                        tuple(barnBarnehageplassStatus, barnehageplass.barnBarnehageplassStatus.getKey()));
    }
}