package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Barnehageplass;
import no.nav.kontantstotte.tekst.DefaultTekstProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class BarnehageplassMappingTest {

    private static final Map<String, String> TEKSTER = new DefaultTekstProvider().hentTekster("nb");
    private static final String hentTekst(Tekstnokkel tekstnokkel) { return TEKSTER.get(tekstnokkel.getNokkel()); }

    private static final String NEI = hentTekst(SVAR_NEI);
    private static final String TITTEL = hentTekst(BARNEHAGEPLASS_TITTEL);
    private static final String BARN_HAR_BARNEHAGEPLASS = hentTekst(HAR_BARNEHAGEPLASS);
    private static final String BARN_BARNEHAGEPLASS_STATUS_SPORMAL = hentTekst(BARN_BARNEHAGEPLASS_STATUS);
    private static final String BARN_BARNEHAGEPLASS_STATUS_SVAR = hentTekst(GAR_IKKE_I_BARNEHAGE);
    private static final String ANTALL_TIMER = hentTekst(HAR_BARNEHAGEPLASS_ANTALL_TIMER);
    private static final String HOYT_TIMEANTALL_ADVARSEL = hentTekst(BARNEHAGEPLASS_HOYT_TIMEANTALL_ADVARSEL);

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
    public void at_over_34_timer_gir_advarsel() {

        barnehageplass.barnBarnehageplassStatus = Barnehageplass.BarnehageplassVerdier.harBarnehageplass;
        barnehageplass.harBarnehageplassAntallTimer = "34";

        List<Element> elementer = barnehageplassMapping.map(soknad).elementer;

        assertThat(elementer)
                .extracting("sporsmal", "svar", "advarsel")
                .contains(
                        tuple(ANTALL_TIMER, "34", HOYT_TIMEANTALL_ADVARSEL));

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
        barnehageplass.harBarnehageplass = "NEI";
        barnehageplass.barnBarnehageplassStatus = Barnehageplass.BarnehageplassVerdier.garIkkeIBarnehage;

        Bolk bolk = barnehageplassMapping.map(soknad);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(BARN_HAR_BARNEHAGEPLASS, NEI),
                        tuple(BARN_BARNEHAGEPLASS_STATUS_SPORMAL, BARN_BARNEHAGEPLASS_STATUS_SVAR));
    }
}
