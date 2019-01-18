package no.nav.kontantstotte.storage.attachment;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AttachmentConverterTest {

    private ImageConversionService imageConversionService = mock(ImageConversionService.class);
    private AttachmentToStorableFormatConverter converter = new AttachmentToStorableFormatConverter(imageConversionService);

    @Test(expected = AttachmentConversionException.class)
    public void at_ulovlig_format_kaster_exception() throws AttachmentConversionException, IOException {
        byte[] txtVedlegg = toByteArray("dummy/txt_dummy.txt");
        converter.toStorageFormat(txtVedlegg);
    }

    @Test
    public void at_lovlig_format_konverteres() throws IOException {
        byte[] converted;
        byte[] pdfDummy = toByteArray("dummy/pdf_dummy.pdf");

        converted = converter.toStorageFormat(pdfDummy);
        assertThat(converted).isEqualTo(pdfDummy);

        when(imageConversionService.convert(any(), any())).thenReturn(pdfDummy);
        byte[] jpgVedlegg = toByteArray("dummy/jpg_dummy.jpg");
        converted = converter.toStorageFormat(jpgVedlegg);
        assertThat(converted).isEqualTo(pdfDummy);
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
