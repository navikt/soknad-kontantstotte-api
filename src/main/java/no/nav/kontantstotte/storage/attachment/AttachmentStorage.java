package no.nav.kontantstotte.storage.attachment;

import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.encryption.EncryptedStorage;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class AttachmentStorage implements Storage {

    private final EncryptedStorage delegate;
    private final AttachmentToStorableFormatConverter storableFormatConverter;

    AttachmentStorage(EncryptedStorage storage, AttachmentToStorableFormatConverter storableFormatConverter) {
        this.delegate = storage;
        this.storableFormatConverter = storableFormatConverter;
    }

    @Override
    public void put(String directory, String key, InputStream data) {
        byte[] storeable = storableFormatConverter.toStorageFormat(toByteArray(data));
        delegate.put(directory, key, new ByteArrayInputStream(storeable));
    }

    @Override
    public Optional<byte[]> get(String directory, String key) {
        return delegate.get(directory, key);
    }

    private byte[] toByteArray(InputStream data) {
        try {
            return IOUtils.toByteArray(data);
        } catch (IOException e) {
            throw new AttachmentConversionException("Kunnne ikke lese inputstream", e);
        }
    }
}
