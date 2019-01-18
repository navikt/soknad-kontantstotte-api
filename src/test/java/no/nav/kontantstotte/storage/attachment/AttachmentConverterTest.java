package no.nav.kontantstotte.storage.attachment;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AttachmentConverterTest {

    private ImageConversionService imageConversionService = mock(ImageConversionService.class);
    private AttachmentToStorableFormatConverter converter = new AttachmentToStorableFormatConverter(imageConversionService);
    byte[] convertedDummy;

    @Before
    public void setUp() throws IOException {
        convertedDummy = toByteArray("dummy/pdf_dummy.pdf");
        when(imageConversionService.convert(any(), any())).thenReturn(convertedDummy);
    }
    @Test(expected = AttachmentConversionException.class)
    public void at_ulovlig_format_kaster_exception() throws AttachmentConversionException, IOException {
        byte[] txtVedlegg = toByteArray("dummy/txt_dummy.txt");
        converter.toStorageFormat(txtVedlegg);
    }

    @Test
    public void at_pdf_aksepteres() throws IOException {
        byte[] pdfVedlegg = toByteArray("dummy/pdf_dummy.pdf");
        byte[] storable = converter.toStorageFormat(pdfVedlegg);
        assertThat(storable).isEqualTo(pdfVedlegg);
    }

    @Test
    public void at_bilder_konverteres() throws IOException {
        byte[] converted;
        byte[] vedlegg;

        vedlegg = toByteArray("dummy/jpg_dummy.jpg");
        converted = converter.toStorageFormat(vedlegg);
        assertThat(converted).isEqualTo(convertedDummy);

        vedlegg= toByteArray("dummy/png_dummy.png");
        converted = converter.toStorageFormat(vedlegg);
        assertThat(converted).isEqualTo(convertedDummy);
    }

    private byte[] toByteArray(String filename) throws IOException {
        File vedleggsfil = new File(getClass().getClassLoader().getResource(filename).getFile());
        ByteArrayInputStream inputStream =  new ByteArrayInputStream(FileUtils.readFileToByteArray(vedleggsfil));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[65536];
        while ((read = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, read);
        }
        return buffer.toByteArray();
    }
}
