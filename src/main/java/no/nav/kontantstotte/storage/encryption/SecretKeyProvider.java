package no.nav.kontantstotte.storage.encryption;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

class SecretKeyProvider {

    private String passphrase;

    SecretKeyProvider(String passphrase) {
        this.passphrase = passphrase;
    }

    SecretKey key(String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(), 10000, 256);
        SecretKey key = factory.generateSecret(spec);
        return new SecretKeySpec(key.getEncoded(), "AES");
    }

}
