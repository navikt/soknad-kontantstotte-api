package no.nav.kontantstotte.storage;

import java.io.InputStream;
import java.util.Optional;

public interface Storage {

    void put(String directory, String key, InputStream data);

    Optional<byte[]> get(String directory, String key);

}
