package no.nav.kontantstotte.innsending.oppsummering;

import no.nav.kontantstotte.innsending.oppsummering.html.OppsummeringHtmlTestConfiguration;
import org.springframework.context.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Import(OppsummeringHtmlTestConfiguration.class)
public class OppsummeringTestConfiguration {

    @Bean
    @Profile("mockgen-pdf")
    @Primary
    PdfConverter pdfGenService() throws IOException {
        PdfConverter service = mock(PdfConverter.class);
        byte[] b = readFile("dummy/pdf_dummy.pdf");
        when(service.genererPdf(any())).thenReturn(b);
        return service;
    }


    private byte[] readFile(String filename) throws IOException {
        File file = new File(getClass().getClassLoader().getResource(filename).getFile());
        RandomAccessFile f = new RandomAccessFile(file, "r");
        byte[] b = new byte[(int)f.length()];
        f.readFully(b);
        return b;
    }
}
