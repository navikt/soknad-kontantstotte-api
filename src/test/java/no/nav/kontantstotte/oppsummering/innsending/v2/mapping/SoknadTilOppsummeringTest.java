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


public class SoknadTilOppsummeringTest {

    public static final String JA = "Ja";
    public static final String NEI = "Nei";

    @Test
    public void bolkerIRettRekkefølge() {


        String fnr = "XXXXXXXXXX";
        SoknadOppsummering oppsummering = new SoknadTilOppsummering().map(
                new Soknad(),
                tekster(
                        tekst(SoknadTilOppsummering.BARN_TITTEL, SoknadTilOppsummering.BARN_TITTEL),
                        tekst(SoknadTilOppsummering.FAMILIEFORHOLD_TITTEL, SoknadTilOppsummering.FAMILIEFORHOLD_TITTEL)
                ),
                fnr);

        assertThat(oppsummering.getBolker())
                .extracting("bolknavn", "tittel")
                .containsSequence(
                        tuple("kravTilSoker", null),
                        tuple(null, SoknadTilOppsummering.BARN_TITTEL),
                        tuple("barnehageplass", null),
                        tuple(null, SoknadTilOppsummering.FAMILIEFORHOLD_TITTEL),
                        tuple("tilknytningTilUtland", null),
                        tuple("arbeidIUtlandet", null),
                        tuple("utenlandskeYtelser", null),
                        tuple("utenlandskKontantstotte", null),
                        tuple("oppsummering", null)
                );
        assertThat(oppsummering.getFnr()).isEqualTo(fnr);
    }


    @Test
    public void tilBarneBolk() {
        String tittel = "BARN";
        String undertittel = "Barn du søker kontantstøtte for";
        String navn = "Navn";
        String fodselsdato = "Fødselsdato";

        Map<String, String> tekster = tekster(
                tekst(SoknadTilOppsummering.BARN_TITTEL, tittel),
                tekst(SoknadTilOppsummering.BARN_UNDERTITTEL, undertittel),
                tekst(SoknadTilOppsummering.BARN_NAVN, navn),
                tekst(SoknadTilOppsummering.BARN_FODSELSDATO, fodselsdato));

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
    public void familieforhold_nar_foreldre_ikke_bor_sammen() {
        String tittel = "Familieforhold";
        String sporsmal = "Bor du sammen med den andre forelderen?";

        Map<String, String> tekster = tekster(
                tekst(SoknadTilOppsummering.FAMILIEFORHOLD_TITTEL, tittel),
                tekst(SoknadTilOppsummering.FAMILIEFORHOLD_BOR_SAMMEN, sporsmal),
                tekst(SoknadTilOppsummering.SVAR_NEI, NEI));


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
                        tuple(sporsmal, NEI));


    }

    @Test
    public void familieforhold_nar_foreldre_bor_sammen() {
        String sporsmal = "Bor du sammen med den andre forelderen?";
        String sporsmal_navn = "Navnet til den andre forelderen:";
        String sporsmal_fnr = "Fødselsnummeret til den andre forelderen:";

        Map<String, String> tekster = tekster(
                tekst(SoknadTilOppsummering.FAMILIEFORHOLD_BOR_SAMMEN, sporsmal),
                tekst(SoknadTilOppsummering.FAMILIEFORHOLD_NAVN_ANNEN_FORELDER, sporsmal_navn),
                tekst(SoknadTilOppsummering.FAMILIEFORHOLD_FNR_ANNEN_FORELDER, sporsmal_fnr),
                tekst(SoknadTilOppsummering.SVAR_JA, JA));


        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "JA";
        familieforhold.annenForelderNavn = "NN";
        familieforhold.annenForelderFodselsnummer = "XXXXXX";
        Bolk bolk = new SoknadTilOppsummering().mapFamilieforhold(familieforhold, tekster);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(sporsmal, JA),
                        tuple(sporsmal_navn, familieforhold.annenForelderNavn),
                        tuple(sporsmal_fnr, familieforhold.annenForelderFodselsnummer)
                );


    }

    private Map<String, String> tekster(AbstractMap.SimpleEntry<String, String>... tekst) {
        return Collections.unmodifiableMap(Stream.of(tekst)
                .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));
    }

    private AbstractMap.SimpleEntry<String, String> tekst(String nokkel, String tekstinnhold) {
        return new AbstractMap.SimpleEntry<>(nokkel, tekstinnhold);
    }


}