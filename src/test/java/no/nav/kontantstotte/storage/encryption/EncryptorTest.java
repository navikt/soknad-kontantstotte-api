package no.nav.kontantstotte.storage.encryption;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class EncryptorTest {

    private static final String originalTekst = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    private static final String fnr = "dummyfnr";

    private SecretKeyProvider secretKeyProvider = new SecretKeyProvider("Passphrase eller passord eller kall det hva du vil...");

    private Encryptor encryptor = new Encryptor(secretKeyProvider);

    @Test
    public void at_encrypt_og_decrypt_fungerer() throws IOException {

        InputStream encryptedStream = encryptor.encryptedStream(fnr, new ByteArrayInputStream(originalTekst.getBytes()));

        byte[] encrypted = toByteArray(encryptedStream);
        assertThat(encrypted).isNotEqualTo(originalTekst.getBytes());

        byte[] decrypted = encryptor.decrypt(fnr, encrypted);
        assertThat(decrypted).isEqualTo(originalTekst.getBytes());
    }

    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[65536];

        while ((read = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, read);
        }

        return buffer.toByteArray();
    }

}
