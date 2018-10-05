package no.nav.kontantstotte.oppsummering.innsending.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadOppsummering;
import no.nav.kontantstotte.tekst.TekstProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SoknadOppsummeringJsonTest {

    private NodeOppsummeringGenerator nodeOppsummeringGenerator;
    private HtmlOppsummeringService htmlOppsummeringService;
    private ObjectMapper mapper;

    @Before
    public void setup(){
        htmlOppsummeringService = mock(HtmlOppsummeringService.class);

        nodeOppsummeringGenerator = new NodeOppsummeringGenerator(
                new TekstProvider("mapping_tekster", "nb"),
                htmlOppsummeringService,
                mock(PdfGenService.class));

        mapper = new ObjectMapper();
    }

    @Test
    public void enkelt_soknadjson_gir_forventet_oppsummeringsobjekt() throws IOException {
        Soknad enkelSoknad = mapper.readValue(new File(getFile("soknad.json")), Soknad.class);
        SoknadOppsummering oppsummering = mapper.readValue(new File(getFile("soknad-oppsummering.json")), SoknadOppsummering.class);

        ArgumentCaptor<SoknadOppsummering> captor = ArgumentCaptor.forClass(SoknadOppsummering.class);
        when(htmlOppsummeringService.genererHtml(captor.capture())).thenReturn(new byte[1]);
        nodeOppsummeringGenerator.genererOppsummering(enkelSoknad, "XXXXXXXXXX");

         assertThat(captor.getValue()).isEqualToComparingFieldByFieldRecursively(oppsummering);
    }

    private String getFile(String filnavn) {
        return getClass().getClassLoader().getResource(filnavn).getFile();
    }

}