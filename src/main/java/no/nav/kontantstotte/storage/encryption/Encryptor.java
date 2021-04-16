package no.nav.kontantstotte.storage.encryption;

import no.nav.kontantstotte.storage.StorageException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.GCMParameterSpec;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public class Encryptor {

    private static final String ALGORITHM = "AES/GCM/NoPadding";

    private final SecretKeyProvider secretKeyProvider;

    Encryptor(SecretKeyProvider secretKeyProvider) {
        this.secretKeyProvider = secretKeyProvider;
    }

    public InputStream encryptedStream(String fnr, InputStream inputStream) {
        return new CipherInputStream(inputStream, initCipher(Cipher.ENCRYPT_MODE, fnr));
    }

    public byte[] decrypt(String fnr, byte[] input) {
        return transform(Cipher.DECRYPT_MODE, fnr, input);
    }

    private byte[] transform(int cipherTransformation, String fnr, byte[] input) {
        try {
            Cipher cipher = initCipher(cipherTransformation, fnr);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw new StorageException("Kunne ikke opprette cipher", e);
        }
    }

    private Cipher initCipher(int cipherTransformation, String fnr) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(cipherTransformation, secretKeyProvider.key(fnr), new GCMParameterSpec(128, fnr.getBytes()));
            return cipher;
        } catch (GeneralSecurityException e) {
            throw new StorageException("Kunne ikke opprette cipher", e);
        }
    }

}
