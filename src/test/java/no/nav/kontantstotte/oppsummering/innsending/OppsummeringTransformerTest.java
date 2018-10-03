package no.nav.kontantstotte.oppsummering.innsending;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.nav.kontantstotte.api.rest.TeksterResource;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.v2.HtmlOppsummeringService;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadOppsummering;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering;
import no.nav.sbl.rest.ClientLogFilter;
import no.nav.sbl.rest.RestUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.ext.ContextResolver;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;

public class OppsummeringTransformerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void generateHtml() throws IOException, URISyntaxException {

        Soknad soknad = new Soknad();
        soknad.sprak = "nb";
        soknad.markerInnsendingsTidspunkt();

        soknad.person.fnr = "DUMMY_FNR";
        soknad.kravTilSoker.barnIkkeHjemme = "JA";
        soknad.kravTilSoker.boddEllerJobbetINorgeSisteFemAar = "JA";
        soknad.kravTilSoker.borSammenMedBarnet = "JA";
        soknad.kravTilSoker.ikkeAvtaltDeltBosted = "NEI";
        soknad.kravTilSoker.norskStatsborger = "JA";
        soknad.kravTilSoker.skalBoMedBarnetINorgeNesteTolvMaaneder = "JA";
        soknad.familieforhold.borForeldreneSammenMedBarnet = "JA";
        soknad.familieforhold.annenForelderNavn = "Annen forelder";
        soknad.familieforhold.annenForelderFodselsnummer = "DUMMY_FNR2";
        soknad.familieforhold.annenForelderYrkesaktivINorgeEOSIMinstFemAar = "NEI";
        soknad.barnehageplass.harBarnehageplass = "NEI";
        soknad.arbeidIUtlandet.arbeiderAnnenForelderIUtlandet = "NEI";
        soknad.utenlandskKontantstotte.mottarKontantstotteFraUtlandet = "NEI";
        soknad.mineBarn.fodselsdato = "11.11.2017";
        soknad.mineBarn.navn = "Første Fødte";
        soknad.tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar = "JA";
        soknad.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland = "NEI";
        soknad.oppsummering.bekreftelse = "JA";

        HtmlOppsummeringService htmlOppsummeringService = new HtmlOppsummeringService(
                ClientBuilder.newBuilder()
                        .register(new ClientLogFilter(ClientLogFilter.ClientLogFilterConfig.builder().metricName("").build()))
                        .register(new ContextResolver<ObjectMapper>() {
                            @Override
                            public ObjectMapper getContext(Class<?> type) {
                                return new ObjectMapper()
                                        .registerModule(new JavaTimeModule())
                                        .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                            }
                        })
                        .register(new LoggingFeature())
                        .build(),
                new URI("http://localhost:9000/api")
        );

        Map<String, String> tekster = new TeksterResource().tekster("nb");
        SoknadOppsummering oppsummering = new SoknadTilOppsummering().map(soknad, tekster);
        byte[] htmlBytes = htmlOppsummeringService.genererHtml(oppsummering);
        String htmlString = new String(htmlBytes);

        System.out.println(htmlString);



        ClientConfig clientConfig = RestUtils
                .createClientConfig()
                .register(new LoggingFeature());

        byte[] pdfBytes = ClientBuilder.newBuilder().withConfig(clientConfig).build()
                .target("http://localhost:9090")
                .path("api/convert")
                .request()
                .buildPost(Entity.entity(htmlString, "text/html; charset=utf-8"))
                .invoke()
                .readEntity(byte[].class);

        // cleanup
        Arrays.stream(new File("target").listFiles((f, p) -> p.endsWith("pdf"))).forEach(File::delete);
        Arrays.stream(new File("target").listFiles((f, p) -> p.endsWith("html"))).forEach(File::delete);

        File htmlFile = File.createTempFile("pdf_", ".html", new File("target"));
        PrintWriter htmlWriter = new PrintWriter(htmlFile);
        File pdfFile = File.createTempFile("pdf_", ".pdf", new File("target"));
        PrintWriter pdfWriter = new PrintWriter(pdfFile);

        Files.write(htmlFile.toPath(), htmlString.getBytes());
        Files.write(pdfFile.toPath(), pdfBytes);
        htmlWriter.close();
        pdfWriter.close();

    }

}
