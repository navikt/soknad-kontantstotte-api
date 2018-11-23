package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.finn.unleash.FakeUnleash;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Barnehageplass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.mockTekster;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.TekstHelper.tekst;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class BarnehageplassMappingTest {

    private static final String JA = "Ja";
    private static final String NEI = "Nei";
    private static final String TITTEL = "BARNEHAGEPLASS";
    private static final String HAR_BARNEHAGEPLASS = "Har barnet barnehageplass?";
    private static final String BARN_BARNEHAGEPLASS_STATUS_SPORMAL = "Barnet mitt";
    private static final String BARN_BARNEHAGEPLASS_STATUS_SVAR = "går ikke i barnehage";
    private static final String ANTALL_TIMER = "Antall timer?";
    private static final String HOYT_TIMEANTALL_ADVARSEL = "For høyt advarsel";


    private static final Map<String, String> TEKSTER = mockTekster(
            tekst(BARNEHAGEPLASS_TITTEL, TITTEL),
            tekst(Tekstnokkel.HAR_BARNEHAGEPLASS, HAR_BARNEHAGEPLASS),
            tekst(BARN_BARNEHAGEPLASS_STATUS, BARN_BARNEHAGEPLASS_STATUS_SPORMAL),
            tekst(GAR_IKKE_I_BARNEHAGE, BARN_BARNEHAGEPLASS_STATUS_SVAR),
            tekst(SVAR_NEI, NEI),
            tekst(Tekstnokkel.HAR_BARNEHAGEPLASS_ANTALL_TIMER, ANTALL_TIMER),
            tekst(Tekstnokkel.BARNEHAGEPLASS_HOYT_TIMEANTALL_ADVARSEL, HOYT_TIMEANTALL_ADVARSEL));

    private Soknad soknad;
    private Barnehageplass barnehageplass;
    private BarnehageplassMapping barnehageplassMapping;

    private FakeUnleash fakeUnleash;

    @Before
    public void setUp() {

        fakeUnleash = new FakeUnleash();
        UnleashProvider.initialize(fakeUnleash);

        barnehageplass = new Barnehageplass();
        soknad = new Soknad.Builder()
                .barnehageplass(barnehageplass)
                .build();
        barnehageplassMapping = new BarnehageplassMapping(TEKSTER);
    }

    @After
    public void tearDown() {
        UnleashProvider.initialize(null);
    }

    @Test
    public void at_oppsummering_advarsel_paa_gir_advarsel() {
        fakeUnleash.enable(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL);

        barnehageplass.barnBarnehageplassStatus = Barnehageplass.BarnehageplassVerdier.harBarnehageplass;
        barnehageplass.harBarnehageplassAntallTimer = 34;

        List<Element> elementer = barnehageplassMapping.map(soknad).elementer;

        assertThat(elementer)
                .extracting("sporsmal", "svar", "advarsel")
                .contains(
                        tuple(ANTALL_TIMER, "34", HOYT_TIMEANTALL_ADVARSEL));

    }

    @Test
    public void at_oppsummering_advarsel_av_ikke_gir_advarsel() {
        fakeUnleash.disable(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL);

        barnehageplass.barnBarnehageplassStatus = Barnehageplass.BarnehageplassVerdier.harBarnehageplass;
        barnehageplass.harBarnehageplassAntallTimer = 34;

        List<Element> elementer = barnehageplassMapping.map(soknad).elementer;

        assertThat(elementer)
                .extracting("sporsmal", "svar", "advarsel")
                .contains(
                        tuple(ANTALL_TIMER, "34", null));

    }

    @Test
    public void skal_ha_rett_tittel_og_undertittel() {
        Bolk bolk = barnehageplassMapping.map(soknad);
        assertThat(bolk)
                .extracting("tittel")
                .containsExactly(TITTEL);

    }

    @Test
    public void skal_ha_rett_tekstnokler_naar_barnet_ikke_gaar_i_barnehage() {
        barnehageplass.harBarnehageplass = false;
        barnehageplass.barnBarnehageplassStatus = Barnehageplass.BarnehageplassVerdier.garIkkeIBarnehage;

        Bolk bolk = barnehageplassMapping.map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(HAR_BARNEHAGEPLASS, NEI),
                        tuple(BARN_BARNEHAGEPLASS_STATUS_SPORMAL, BARN_BARNEHAGEPLASS_STATUS_SVAR));
    }
}