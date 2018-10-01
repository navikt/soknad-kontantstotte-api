package no.nav.kontantstotte.oppsummering.innsending;

import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.sbl.rest.RestUtils;
import no.nav.security.oidc.jaxrs.OidcClientRequestFilter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;

public class OppsummeringTransformerTest {

    private OppsummeringTransformer transformer = new OppsummeringTransformer();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void generateHtml() throws IOException {

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
        String htmlString = transformer.renderHTMLForPdf(soknad);

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
