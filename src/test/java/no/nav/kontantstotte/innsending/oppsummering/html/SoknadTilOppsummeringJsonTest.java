package no.nav.kontantstotte.innsending.oppsummering.html;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.finn.unleash.FakeUnleash;
import no.nav.kontantstotte.api.rest.dto.SoknadConverter;
import no.nav.kontantstotte.api.rest.dto.SoknadDto;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.tekst.TekstProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.format.datetime.joda.DateTimeParser;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static org.assertj.core.api.Assertions.assertThat;

public class SoknadTilOppsummeringJsonTest {

    private ObjectMapper mapper;
    private SoknadTilOppsummering soknadTilOppsummering;

    private FakeUnleash fakeUnleash = new FakeUnleash();

    private SoknadConverter converter = new SoknadConverter();

    @Before
    public void setup() {
        fakeUnleash.enable(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL);
        UnleashProvider.initialize(fakeUnleash);

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
        SoknadDto soknad = mapper.readValue(new File(getFile("mapping/enkel/soknad.json")), SoknadDto.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/enkel/soknad-oppsummering.json")), SoknadOppsummering.class);

        Soknad soknadFromDto = converter.convert(soknad);
        soknadFromDto.setInnsendingsTidspunkt(Instant.from(DateTimeFormatter.ISO_INSTANT.parse("2018-10-10T11:23:03.181Z")));
        SoknadOppsummering actual = soknadTilOppsummering.map(soknadFromDto, "XXXXXXXXXX");

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    @Test
    public void komplett_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        SoknadDto soknad = mapper.readValue(new File(getFile("mapping/komplett/soknad.json")), SoknadDto.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/komplett/soknad-oppsummering.json")), SoknadOppsummering.class);

        Soknad soknadFromDto = converter.convert(soknad);
        soknadFromDto.setInnsendingsTidspunkt(Instant.from(DateTimeFormatter.ISO_INSTANT.parse("2018-10-10T11:23:03.181Z")));
        SoknadOppsummering actual = soknadTilOppsummering.map(soknadFromDto, "XXXXXXXXXX");

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    @Test
    public void ekstrem_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        SoknadDto soknad = mapper.readValue(new File(getFile("mapping/ekstrem/soknad.json")), SoknadDto.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/ekstrem/soknad-oppsummering.json")), SoknadOppsummering.class);

        Soknad soknadFromDto = converter.convert(soknad);
        soknadFromDto.setInnsendingsTidspunkt(Instant.from(DateTimeFormatter.ISO_INSTANT.parse("2018-10-10T11:23:03.181Z")));
        SoknadOppsummering actual = soknadTilOppsummering.map(soknadFromDto, "XXXXXXXXXX");

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    private String getFile(String filnavn) {
        return getClass().getClassLoader().getResource(filnavn).getFile();
    }
}
