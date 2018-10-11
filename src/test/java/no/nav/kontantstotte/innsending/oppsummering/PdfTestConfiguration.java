package no.nav.kontantstotte.innsending.oppsummering;

import no.nav.kontantstotte.innsending.oppsummering.html.HtmlConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class PdfTestConfiguration {

    @Bean
    @Primary
    PdfConverter pdfGenService() throws IOException {
        PdfConverter service = mock(PdfConverter.class);
        byte[] b = readFile("oppsummering.pdf");
        when(service.genererPdf(any())).thenReturn(b);
        return service;
    }


    @Bean
    @Primary
    HtmlConverter htmlOppsummeringService() throws IOException {
        HtmlConverter service = mock(HtmlConverter.class);
        byte[] b = readFile("oppsummering.html");
        when(service.genererHtml(any())).thenReturn(b);
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