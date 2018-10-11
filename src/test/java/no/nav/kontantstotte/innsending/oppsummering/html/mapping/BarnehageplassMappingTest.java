package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.finn.unleash.FakeUnleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Barnehageplass;
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


    @Test
    public void tilBarnehageplassBolk() {
        String tittel = "BARNEHAGEPLASS";
        String harBarnehageplass = "Har barnet barnehageplass?";
        String barnBarnehageplassStatusSpormal = "Barnet mitt";
        String barnBarnehageplassStatusSvar = "går ikke i barnehage";

        Map<String, String> tekster = mockTekster(
                tekst(BARNEHAGEPLASS_TITTEL, tittel),
                tekst(HAR_BARNEHAGEPLASS, harBarnehageplass),
                tekst(BARN_BARNEHAGEPLASS_STATUS, barnBarnehageplassStatusSpormal),
                tekst(GAR_IKKE_I_BARNEHAGE, barnBarnehageplassStatusSvar),
                tekst(SVAR_NEI, NEI));

        Soknad soknad = new Soknad();
        Barnehageplass barnehageplass = new Barnehageplass();
        barnehageplass.harBarnehageplass = "NEI";
        barnehageplass.barnBarnehageplassStatus = Barnehageplass.BarnehageplassVerdier.garIkkeIBarnehage;
        soknad.barnehageplass = barnehageplass;

        Bolk bolk = new BarnehageplassMapping(tekster).map(soknad, new FakeUnleash());
        assertThat(bolk)
                .extracting("tittel")
                .containsExactly(tittel);

        List<Element> elementer = bolk.elementer;
        assertThat(elementer)
                .extracting("sporsmal", "svar")
                .contains(
                        tuple(harBarnehageplass, NEI),
                        tuple(barnBarnehageplassStatusSpormal, barnBarnehageplassStatusSvar));
    }
}