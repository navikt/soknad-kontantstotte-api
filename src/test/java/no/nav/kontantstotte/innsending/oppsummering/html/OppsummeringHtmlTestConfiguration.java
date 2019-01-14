package no.nav.kontantstotte.innsending.oppsummering.html;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Profile("mockgen-pdf")
public class OppsummeringHtmlTestConfiguration {

    @Bean
    @Primary
    HtmlConverter htmlConverter() throws IOException {
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
