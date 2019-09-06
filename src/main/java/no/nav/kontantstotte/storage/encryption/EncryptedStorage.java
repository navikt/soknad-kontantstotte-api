package no.nav.kontantstotte.storage.encryption;

import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.s3.S3Storage;

import java.io.InputStream;
import java.util.Optional;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

public class EncryptedStorage implements Storage {

    private final S3Storage delegate;
    private final Encryptor encryptor;

    public EncryptedStorage(S3Storage storage, Encryptor encryptor) {
        this.delegate = storage;
        this.encryptor = encryptor;
    }

    @Override
    public void put(String directory, String key, InputStream data) {
        delegate.put(directory, key, encryptor.encryptedStream(hentFnrFraToken(), data));
    }

    @Override
    public Optional<byte[]> get(String directory, String key) {
        return delegate.get(directory, key)
                .map(content ->
                        encryptor.decrypt(hentFnrFraToken(), content));
    }

}
