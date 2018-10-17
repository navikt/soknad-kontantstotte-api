package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.finn.unleash.FakeUnleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Barnehageplass;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

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

    private static final Map<String, String> TEKSTER = mockTekster(
            tekst(BARNEHAGEPLASS_TITTEL, TITTEL),
            tekst(Tekstnokkel.HAR_BARNEHAGEPLASS, HAR_BARNEHAGEPLASS),
            tekst(BARN_BARNEHAGEPLASS_STATUS, BARN_BARNEHAGEPLASS_STATUS_SPORMAL),
            tekst(GAR_IKKE_I_BARNEHAGE, BARN_BARNEHAGEPLASS_STATUS_SVAR),
            tekst(SVAR_NEI, NEI));
    public static final FakeUnleash UNLEASH = new FakeUnleash();
    private Soknad soknad;
    private Barnehageplass barnehageplass;
    private BarnehageplassMapping barnehageplassMapping;

    @Before
    public void setUp() {
        barnehageplass = new Barnehageplass();
        soknad = new Soknad();
        soknad.barnehageplass = barnehageplass;
        barnehageplassMapping = new BarnehageplassMapping(TEKSTER);
    }

    @Test
    public void skal_ha_rett_tittel_og_undertittel() {
        Bolk bolk = barnehageplassMapping.map(soknad, UNLEASH);
        assertThat(bolk)
                .extracting("tittel")
                .containsExactly(TITTEL);

    }

    @Test
    public void skal_ha_rett_tekstnokler_naar_barnet_ikke_gaar_i_barnehage() {
        barnehageplass.harBarnehageplass = "NEI";
        barnehageplass.barnBarnehageplassStatus = Barnehageplass.BarnehageplassVerdier.garIkkeIBarnehage;

        Bolk bolk = barnehageplassMapping.map(soknad, UNLEASH);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(HAR_BARNEHAGEPLASS, NEI),
                        tuple(BARN_BARNEHAGEPLASS_STATUS_SPORMAL, BARN_BARNEHAGEPLASS_STATUS_SVAR));
    }
}