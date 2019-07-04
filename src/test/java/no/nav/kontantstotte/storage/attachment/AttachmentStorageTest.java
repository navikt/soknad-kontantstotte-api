package no.nav.kontantstotte.storage.attachment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import no.nav.kontantstotte.storage.Storage;

public class AttachmentStorageTest {

    private Storage delegate = mock(Storage.class);
    private AttachmentToStorableFormatConverter converter = mock(AttachmentToStorableFormatConverter.class);
    private AttachmentStorage attachmentStorage = new AttachmentStorage(delegate, converter);

    private byte[] pdfByteArray;
    private String pdfByteString;

    @Before
    public void setUp() throws IOException {
        pdfByteArray = readStream(toStream("dummy/pdf_dummy.pdf")).toByteArray();
        pdfByteString = readStream(toStream("dummy/pdf_dummy.pdf")).toString(StandardCharsets.UTF_8);
        when(converter.toStorageFormat(any(byte[].class))).thenReturn(pdfByteArray);
    }

    @Test
    public void converts_before_put() throws IOException {
        attachmentStorage.put("directory123", "UUID123", toStream("dummy/jpg_dummy.jpg"));

        // Fanger opp argument for å kunne sammenligne innhold fra ny ByteArrayInputStream-instans
        ArgumentCaptor<ByteArrayInputStream> streamCaptor = ArgumentCaptor.forClass(ByteArrayInputStream.class);
        verify(delegate).put(eq("directory123"), eq("UUID123"), streamCaptor.capture());
        String capturedStream = readStream(streamCaptor.getValue()).toString(StandardCharsets.UTF_8);
        assertThat(capturedStream).isEqualTo(pdfByteString);
    }

    @Test
    public void converted_after_get() throws IOException {
        Optional<byte[]> optionalByteArray = Optional.ofNullable(pdfByteArray);
        when(delegate.get("directory123", "UUID123")).thenReturn(optionalByteArray);

        attachmentStorage.put("directory123", "UUID123", toStream("dummy/pdf_dummy.pdf"));
        assertThat(attachmentStorage.get("directory123", "UUID123")).isEqualTo(optionalByteArray);
        assertThat(attachmentStorage.get("directory123", "UUID1234")).isEmpty();
        assertThat(attachmentStorage.get("directory1234", "UUID123")).isEmpty();
    }

    private ByteArrayInputStream toStream(String filename) throws IOException {
        Objects.requireNonNull(filename, "filename");
        File vedleggsfil = new File("src/test/resources/" + filename);
        if (!vedleggsfil.exists()) {
            throw new IllegalArgumentException("Ikke gyldig fil " + filename);
        }
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
