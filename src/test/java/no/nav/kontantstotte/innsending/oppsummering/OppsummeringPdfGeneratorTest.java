package no.nav.kontantstotte.innsending.oppsummering;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.finn.unleash.FakeUnleash;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.HtmlConverter;
import no.nav.kontantstotte.innsending.oppsummering.html.SoknadOppsummering;
import no.nav.kontantstotte.innsending.oppsummering.html.SoknadTilOppsummering;
import no.nav.kontantstotte.tekst.TekstProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OppsummeringPdfGeneratorTest {

    private OppsummeringPdfGenerator oppsummeringPdfGenerator;
    private HtmlConverter htmlConverter;
    private ObjectMapper mapper;
    private ArgumentCaptor<SoknadOppsummering> captor;

    @Before
    public void setup() {
        FakeUnleash fakeUnleash = new FakeUnleash();
        fakeUnleash.enable(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL);

        htmlConverter = mock(HtmlConverter.class);

        oppsummeringPdfGenerator = new OppsummeringPdfGenerator(
                new TekstProvider("mapping_tekster", "nb"),
                htmlConverter,
                mock(PdfConverter.class),
                new SoknadTilOppsummering(fakeUnleash));

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        captor = ArgumentCaptor.forClass(SoknadOppsummering.class);
    }

    @Test
    public void enkelt_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        Soknad soknad = mapper.readValue(new File(getFile("mapping/enkel/soknad.json")), Soknad.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/enkel/soknad-oppsummering.json")), SoknadOppsummering.class);

        when(htmlConverter.genererHtml(captor.capture())).thenReturn(new byte[1]);
        oppsummeringPdfGenerator.generer(soknad, "XXXXXXXXXX");

        assertThat(captor.getValue()).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    @Test
    public void komplett_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        Soknad soknad = mapper.readValue(new File(getFile("mapping/komplett/soknad.json")), Soknad.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/komplett/soknad-oppsummering.json")), SoknadOppsummering.class);

        when(htmlConverter.genererHtml(captor.capture())).thenReturn(new byte[1]);
        oppsummeringPdfGenerator.generer(soknad, "XXXXXXXXXX");

        assertThat(captor.getValue()).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    @Test
    public void ekstrem_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        Soknad soknad = mapper.readValue(new File(getFile("mapping/ekstrem/soknad.json")), Soknad.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/ekstrem/soknad-oppsummering.json")), SoknadOppsummering.class);

        when(htmlConverter.genererHtml(captor.capture())).thenReturn(new byte[1]);
        oppsummeringPdfGenerator.generer(soknad, "XXXXXXXXXX");

        assertThat(captor.getValue()).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    private String getFile(String filnavn) {
        return getClass().getClassLoader().getResource(filnavn).getFile();
    }
}
