package no.nav.kontantstotte.innsending.oppsummering.html;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.finn.unleash.FakeUnleash;
import no.nav.kontantstotte.EnableFeatureToggle;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.tekst.TekstProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static org.assertj.core.api.Assertions.assertThat;

public class SoknadTilOppsummeringJsonTest {

    private ObjectMapper mapper;
    private SoknadTilOppsummering soknadTilOppsummering;

    @Rule
    public EnableFeatureToggle enableFeatureToggle = new EnableFeatureToggle(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL);

    @Before
    public void setup() {

        soknadTilOppsummering = new SoknadTilOppsummering(new TekstProvider("mapping_tekster", "nb"));

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

        SoknadOppsummering actual = soknadTilOppsummering.map(soknad, "XXXXXXXXXX");

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    @Test
    public void komplett_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        Soknad soknad = mapper.readValue(new File(getFile("mapping/komplett/soknad.json")), Soknad.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/komplett/soknad-oppsummering.json")), SoknadOppsummering.class);

        SoknadOppsummering actual = soknadTilOppsummering.map(soknad, "XXXXXXXXXX");

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    @Test
    public void ekstrem_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        Soknad soknad = mapper.readValue(new File(getFile("mapping/ekstrem/soknad.json")), Soknad.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/ekstrem/soknad-oppsummering.json")), SoknadOppsummering.class);

        SoknadOppsummering actual = soknadTilOppsummering.map(soknad, "XXXXXXXXXX");

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    private String getFile(String filnavn) {
        return getClass().getClassLoader().getResource(filnavn).getFile();
    }
}
