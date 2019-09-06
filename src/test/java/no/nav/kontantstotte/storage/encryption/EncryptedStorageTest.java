package no.nav.kontantstotte.storage.encryption;

import no.nav.kontantstotte.SetOidcClaimsWithSubject;
import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.s3.S3Storage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class EncryptedStorageTest {

    private static final byte[] UNENCRYPTED_DATA = "originalStream".getBytes();
    private static final byte[] ENCRYPTED_DATA = "encryptedStream".getBytes();
    private static final InputStream ENCRYPTED_STREAM = new ByteArrayInputStream(ENCRYPTED_DATA);
    private static final String FNR = "DummyFnr";

    @Rule
    public SetOidcClaimsWithSubject claims = new SetOidcClaimsWithSubject(FNR);

    private S3Storage storage = mock(S3Storage.class);

    private Encryptor encryptor = mock(Encryptor.class);

    private EncryptedStorage encryptedStorage = new EncryptedStorage(storage, encryptor);

    @Before
    public void setUpMockedEncryptor() {

        when(encryptor.encryptedStream(eq(FNR), any(InputStream.class))).thenReturn(ENCRYPTED_STREAM);
        when(encryptor.decrypt(eq(FNR), eq(ENCRYPTED_DATA))).thenReturn(UNENCRYPTED_DATA);

    }

    @Test
    public void encrypts_before_put() {

        encryptedStorage.put("directory", "UUID", new ByteArrayInputStream(UNENCRYPTED_DATA));

        verify(storage).put(eq("directory"), eq("UUID"), eq(ENCRYPTED_STREAM));
    }

    @Test
    public void encrypts_after_get() {

        when(storage.get("directory", "UUID")).thenReturn(Optional.of(ENCRYPTED_DATA));

        Optional<byte[]> fetchedData = encryptedStorage.get("directory", "UUID");

        assertThat(fetchedData).hasValue(UNENCRYPTED_DATA);
    }
}
