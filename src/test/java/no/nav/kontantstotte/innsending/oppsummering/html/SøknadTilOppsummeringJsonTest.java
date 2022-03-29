package no.nav.kontantstotte.innsending.oppsummering.html;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.familie.kontrakter.ks.søknad.Søknad;
import no.nav.familie.kontrakter.ks.søknad.testdata.SøknadTestdata;
import no.nav.kontantstotte.innsending.oppsummering.SøknadOppsummering;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.kontantstotte.tekst.TekstService;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SøknadTilOppsummeringJsonTest {

    private static final String FNR = "XXXXXXXXXXX";
    private ObjectMapper mapper;
    private final InnsynService innsynServiceClient = mock(InnsynService.class);
    private SoknadTilOppsummering søknadTilOppsummering;
    private Map<String, String> tekster;

    @Before
    public void setUp() throws IOException {
        søknadTilOppsummering =
                new SoknadTilOppsummering(new TekstService("mapping_tekster", "mapping_land", "nb"), innsynServiceClient);
        mapper = new ObjectMapper();
        tekster = mapper.readValue(new File(getFile("mapping/ny/tekster.json")), new TypeReference<HashMap<String, String>>() {
        });
    }

    @Test
    public void skal_mappe_riktig_for_familie_uten_medforelder_uten_bhgplass() throws IOException {
        when(innsynServiceClient.hentPersonInfo(any())).thenReturn(new Person.Builder().fornavn("NAVN")
                                                                                       .etternavn("NAVNESEN")
                                                                                       .statsborgerskap("NOR")
                                                                                       .build());

        Søknad søknad = SøknadTestdata.norskFamilieUtenAnnenPartOgUtenBarnehageplass();
        List<Bolk> bolker = Arrays.asList(mapper.readValue(new File(getFile("mapping/ny/utenAnnenPartUtenBarnehageplass.json")),
                                                           Bolk[].class));

        SøknadOppsummering expected = new SøknadOppsummering(søknad, lagPerson(), "02.10.2019 - 10.17", bolker, tekster);
        SøknadOppsummering actual = søknadTilOppsummering.map(søknad, FNR);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void skal_mappe_riktig_for_familie_med_flerlinger() throws IOException {
        when(innsynServiceClient.hentPersonInfo(any())).thenReturn(new Person.Builder().fornavn("NAVN")
                                                                                       .etternavn("NAVNESEN")
                                                                                       .statsborgerskap("NOR")
                                                                                       .build());

        Søknad søknad = SøknadTestdata.norskFamilieUtenBarnehageplassFlerlinger();
        List<Bolk> bolker =
                Arrays.asList(mapper.readValue(new File(getFile("mapping/ny/flerlingerUtenBarnehageplass.json")), Bolk[].class));

        SøknadOppsummering expected = new SøknadOppsummering(søknad, lagPerson(), "02.10.2019 - 10.17", bolker, tekster);
        SøknadOppsummering actual = søknadTilOppsummering.map(søknad, FNR);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void skal_mappe_riktig_for_utenlandsk_familie_med_bhgplass() throws IOException {
        when(innsynServiceClient.hentPersonInfo(any())).thenReturn(new Person.Builder().fornavn("NAVN")
                                                                                       .etternavn("NAVNESEN")
                                                                                       .statsborgerskap("NOR")
                                                                                       .build());

        Søknad søknad = SøknadTestdata.utenlandskFamilieMedGradertBarnehageplass();
        List<Bolk> bolker =
                Arrays.asList(mapper.readValue(new File(getFile("mapping/ny/utenlandskeForeldreMedBarnehageplass.json")),
                                               Bolk[].class));

        SøknadOppsummering expected = new SøknadOppsummering(søknad, lagPerson(), "02.10.2019 - 10.17", bolker, tekster);
        SøknadOppsummering actual = søknadTilOppsummering.map(søknad, FNR);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private String getFile(String filnavn) {
        return "src/test/resources/" + filnavn;
    }

    private no.nav.kontantstotte.innsending.steg.Person lagPerson() {
        Person personFraPdl = innsynServiceClient.hentPersonInfo(FNR);
        return new no.nav.kontantstotte.innsending.steg.Person(FNR, personFraPdl.getFulltnavn(), "Norge");
    }
}
