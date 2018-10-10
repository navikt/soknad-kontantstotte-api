package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;

import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barn;
import no.nav.kontantstotte.oppsummering.bolk.Barnehageplass;
import no.nav.kontantstotte.oppsummering.bolk.Familieforhold;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.BarnMapping;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.BarnehageplassMapping;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.FamilieforholdMapping;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering.SVAR_JA;
import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering.SVAR_NEI;
import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.BarnMapping.*;
import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.BarnehageplassMapping.BARNEHAGEPLASS_TITTEL;
import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.BarnehageplassMapping.BARN_BARNEHAGEPLASS_STATUS;
import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.BarnehageplassMapping.HAR_BARNEHAGEPLASS;
import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker.FamilieforholdMapping.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


public class SoknadTilOppsummeringTest {
    public static final String JA = "Ja";
    public static final String NEI = "Nei";

    private Unleash unleash;

    @Before
    public void init() {
        this.unleash = new FakeUnleash();
    }

    @Test
    public void bolkerIRettRekkefølge() {
        String fnr = "XXXXXXXXXX";
        SoknadOppsummering oppsummering = new SoknadTilOppsummering(unleash).map(
                new Soknad(),
                tekster(
                        tekst(BARN_TITTEL, BARN_TITTEL),
                        tekst(BARNEHAGEPLASS_TITTEL, BARNEHAGEPLASS_TITTEL),
                        tekst(FAMILIEFORHOLD_TITTEL, FAMILIEFORHOLD_TITTEL)
                ),
                fnr);

        assertThat(oppsummering.getBolker())
                .extracting("bolknavn", "tittel")
                .containsSequence(
                        tuple("kravTilSoker", null),
                        tuple(null, BARN_TITTEL),
                        tuple(null, BARNEHAGEPLASS_TITTEL),
                        tuple(null, FAMILIEFORHOLD_TITTEL),
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
                tekst(BARN_TITTEL, tittel),
                tekst(BARN_UNDERTITTEL, undertittel),
                tekst(BARN_NAVN, navn),
                tekst(BARN_FODSELSDATO, fodselsdato));

        Soknad soknad = new Soknad();
        Barn innsendtBarn = new Barn();
        innsendtBarn.navn = "Barnets navn";
        innsendtBarn.fodselsdato = "01.01.2019";
        soknad.mineBarn = innsendtBarn;

        Bolk bolk = new BarnMapping().map(soknad, tekster, unleash);
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
                new AbstractMap.SimpleEntry<>(BARNEHAGEPLASS_TITTEL, tittel),
                new AbstractMap.SimpleEntry<>(HAR_BARNEHAGEPLASS, harBarnehageplass),
                new AbstractMap.SimpleEntry<>(BARN_BARNEHAGEPLASS_STATUS, barnBarnehageplassStatus),
                new AbstractMap.SimpleEntry<>(BARNEHAGEPLASS_GAR_IKKE_I_BARNEHAGE, barnBarnehageplassStatus))
                .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));

        Soknad soknad = new Soknad();
        Barnehageplass barnehageplass = new Barnehageplass();
        barnehageplass.harBarnehageplass = "NEI";
        barnehageplass.barnBarnehageplassStatus = Barnehageplass.BarnehageplassVerdier.garIkkeIBarnehage;
        soknad.barnehageplass = barnehageplass;

        Bolk bolk = new BarnehageplassMapping().map(soknad, tekster, unleash);
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

    @Test
    public void familieforhold_nar_foreldre_ikke_bor_sammen() {
        String tittel = "Familieforhold";
        String sporsmal = "Bor du sammen med den andre forelderen?";

        Map<String, String> tekster = tekster(
                tekst(FAMILIEFORHOLD_TITTEL, tittel),
                tekst(FAMILIEFORHOLD_BOR_SAMMEN, sporsmal),
                tekst(SVAR_NEI, NEI));

        Soknad soknad = new Soknad();
        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "NEI";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new FamilieforholdMapping().map(soknad, tekster, unleash);
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
                tekst(FAMILIEFORHOLD_BOR_SAMMEN, sporsmal),
                tekst(FAMILIEFORHOLD_NAVN_ANNEN_FORELDER, sporsmal_navn),
                tekst(FAMILIEFORHOLD_FNR_ANNEN_FORELDER, sporsmal_fnr),
                tekst(SVAR_JA, JA));

        Soknad soknad = new Soknad();
        Familieforhold familieforhold = new Familieforhold();
        familieforhold.borForeldreneSammenMedBarnet = "JA";
        familieforhold.annenForelderNavn = "NN";
        familieforhold.annenForelderFodselsnummer = "XXXXXX";
        soknad.familieforhold = familieforhold;

        Bolk bolk = new FamilieforholdMapping().map(soknad, tekster, unleash);

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