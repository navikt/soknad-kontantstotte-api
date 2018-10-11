package no.nav.kontantstotte.oppsummering.innsending.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.finn.unleash.FakeUnleash;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadOppsummering;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering;
import no.nav.kontantstotte.tekst.TekstProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_OPPSUMMERING_ADVARSEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SoknadOppsummeringJsonTest {
    private NodeOppsummeringGenerator nodeOppsummeringGenerator;
    private HtmlOppsummeringService htmlOppsummeringService;
    private ObjectMapper mapper;
    private ArgumentCaptor<SoknadOppsummering> captor;

    @Before
    public void setup(){
        FakeUnleash fakeUnleash = new FakeUnleash();
        fakeUnleash.enable(KONTANTSTOTTE_OPPSUMMERING_ADVARSEL);

        htmlOppsummeringService = mock(HtmlOppsummeringService.class);

        nodeOppsummeringGenerator = new NodeOppsummeringGenerator(
                new TekstProvider("mapping_tekster", "nb"),
                htmlOppsummeringService,
                mock(PdfGenService.class),
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

        when(htmlOppsummeringService.genererHtml(captor.capture())).thenReturn(new byte[1]);
        nodeOppsummeringGenerator.genererOppsummering(soknad, "XXXXXXXXXX");

         assertThat(captor.getValue()).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    @Test
    public void komplett_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        Soknad soknad = mapper.readValue(new File(getFile("mapping/komplett/soknad.json")), Soknad.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/komplett/soknad-oppsummering.json")), SoknadOppsummering.class);

        when(htmlOppsummeringService.genererHtml(captor.capture())).thenReturn(new byte[1]);
        nodeOppsummeringGenerator.genererOppsummering(soknad, "XXXXXXXXXX");

        assertThat(captor.getValue()).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    @Test
    public void ekstrem_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        Soknad soknad = mapper.readValue(new File(getFile("mapping/ekstrem/soknad.json")), Soknad.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("mapping/ekstrem/soknad-oppsummering.json")), SoknadOppsummering.class);

        when(htmlOppsummeringService.genererHtml(captor.capture())).thenReturn(new byte[1]);
        nodeOppsummeringGenerator.genererOppsummering(soknad, "XXXXXXXXXX");

        assertThat(captor.getValue()).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    private String getFile(String filnavn) {
        return getClass().getClassLoader().getResource(filnavn).getFile();
    }

}