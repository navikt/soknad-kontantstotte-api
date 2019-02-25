package no.nav.kontantstotte.innsending.oppsummering.html;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.kontantstotte.tekst.TekstService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SoknadTilOppsummeringJsonTest {

    private ObjectMapper mapper;
    private SoknadTilOppsummering soknadTilOppsummering;

    private InnsynService innsynServiceClient = mock(InnsynService.class);

    @Before
    public void setup() {

        soknadTilOppsummering = new SoknadTilOppsummering(new TekstService("mapping_tekster", "mapping_land", "nb"), innsynServiceClient);

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @After
    public void tearDown() {
        UnleashProvider.initialize(null);
    }

    @Test
    public void enkelt_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        Soknad soknad = mapper.readValue(new File(getFile("mapping/enkel/soknad.json")), Soknad.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/enkel/soknad-oppsummering.json")), SoknadOppsummering.class);

        when(innsynServiceClient.hentPersonInfo(any())).thenReturn(new Person.Builder().fornavn("NAVN").slektsnavn("NAVNESEN").build());
        SoknadOppsummering actual = soknadTilOppsummering.map(soknad, "XXXXXXXXXX");

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    @Test
    public void komplett_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        Soknad soknad = mapper.readValue(new File(getFile("mapping/komplett/soknad.json")), Soknad.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/komplett/soknad-oppsummering.json")), SoknadOppsummering.class);

        when(innsynServiceClient.hentPersonInfo(any())).thenReturn(new Person.Builder().fornavn("NAVN").slektsnavn("NAVNESEN").build());
        SoknadOppsummering actual = soknadTilOppsummering.map(soknad, "XXXXXXXXXX");

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    @Test
    public void ekstrem_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        Soknad soknad = mapper.readValue(new File(getFile("mapping/ekstrem/soknad.json")), Soknad.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/ekstrem/soknad-oppsummering.json")), SoknadOppsummering.class);

        when(innsynServiceClient.hentPersonInfo(any())).thenReturn(new Person.Builder().fornavn("NAVN").slektsnavn("NAVNESEN").build());
        SoknadOppsummering actual = soknadTilOppsummering.map(soknad, "XXXXXXXXXX");

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    private String getFile(String filnavn) {
        return getClass().getClassLoader().getResource(filnavn).getFile();
    }
}
