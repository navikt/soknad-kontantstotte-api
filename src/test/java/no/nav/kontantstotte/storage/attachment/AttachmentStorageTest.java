package no.nav.kontantstotte.storage.attachment;

import no.nav.kontantstotte.storage.Storage;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AttachmentStorageTest {

    private Storage delegate = mock(Storage.class);
    private AttachmentToStorableFormatConverter converter = mock(AttachmentToStorableFormatConverter.class);
    private AttachmentStorage attachmentStorage = new AttachmentStorage(delegate, converter);

    private byte[] pdfByteArray;
    private String pdfByteString;

    @Before
    public void setUp() throws IOException {
        pdfByteArray = readStream(toStream("dummy/pdf_dummy.pdf")).toByteArray();
        pdfByteString = readStream(toStream("dummy/pdf_dummy.pdf")).toString("UTF-8");;
        when(converter.toStorageFormat(any(byte[].class))).thenReturn(pdfByteArray);
    }

    @Test
    public void converts_before_put() throws IOException {
        attachmentStorage.put("directory123", "UUID123", toStream("dummy/jpg_dummy.jpg"));

        // Fanger opp argument for å kunne sammenligne innhold fra ny ByteArrayInputStream-instans
        ArgumentCaptor<ByteArrayInputStream> streamCaptor = ArgumentCaptor.forClass(ByteArrayInputStream.class);
        verify(delegate).put(eq("directory123"), eq("UUID123"), streamCaptor.capture());
        String capturedStream = readStream(streamCaptor.getValue()).toString("UTF-8");
        assertThat(capturedStream).isEqualTo(pdfByteString);
    }

    @Test
    public void converted_after_get() throws IOException {
        Optional<byte[]> optionalByteArray = Optional.ofNullable(pdfByteArray);
        when(delegate.get("directory123", "UUID123")).thenReturn(optionalByteArray);

        attachmentStorage.put("directory123","UUID123", toStream("dummy/pdf_dummy.pdf"));
        assertThat(attachmentStorage.get("directory123","UUID123")).isEqualTo(optionalByteArray);
        assertThat(attachmentStorage.get("directory123","UUID1234")).isEmpty();
        assertThat(attachmentStorage.get("directory1234","UUID123")).isEmpty();
    }

    private ByteArrayInputStream toStream(String filename) throws IOException {
        File vedleggsfil = new File(getClass().getClassLoader().getResource(filename).getFile());
        return new ByteArrayInputStream(FileUtils.readFileToByteArray(vedleggsfil));
    }

    private ByteArrayOutputStream readStream(ByteArrayInputStream inputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[65536];
        while ((read = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, read);
        }
        return buffer;
    }
}
