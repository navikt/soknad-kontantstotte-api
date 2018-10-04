package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;

import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barn;
import no.nav.kontantstotte.oppsummering.bolk.Familieforhold;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SoknadTilOppsummeringTest {

    @Test
    public void bolkerIRettRekkefølge() {

        Map<String, String> tekster = mock(Map.class);
        when(tekster.get(any())).thenReturn("tekstinnhold");
        SoknadOppsummering oppsummering = new SoknadTilOppsummering().map(new Soknad(), tekster);

        assertThat(oppsummering.getBolker())
                .extracting("bolknavn")
                .containsSequence(
                        "personalia",
                        "kravTilSoker", 
                        null,
                        "barnehageplass", 
                        "familieforhold", 
                        "tilknytningTilUtland", 
                        "arbeidIUtlandet", 
                        "utenlandskeYtelser", 
                        "utenlandskKontantstotte", 
                        "oppsummering"
                );
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
    public void tilFamilieforholdBolk() {
        String tittel = "Familieforhold";
        String sporsmal = "Bor du sammen med den andre forelderen?" ;

        Map<String, String> tekster = Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>(SoknadTilOppsummering.FAMILIEFORHOLD_TITTEL, tittel),
                new AbstractMap.SimpleEntry<>(SoknadTilOppsummering.FAMILIEFORHOLD_BOR_SAMMEN, "Bor du sammen med den andre forelderen?"),
                new AbstractMap.SimpleEntry<>(SoknadTilOppsummering.SVAR_NEI, "Nei"))
                .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));


        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "NEI";
        Bolk bolk = new SoknadTilOppsummering().mapFamilieforhold(familieforhold, tekster);
        assertThat(bolk)
                .extracting("tittel", "undertittel")
                .containsExactly(tittel, null);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, "Nei"));


    }




}