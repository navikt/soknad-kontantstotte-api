package no.nav.kontantstotte.storage;

import java.util.Optional;

public interface Storage {

    void put(String directory, String key, String value);

    Optional<String> get(String directory, String key);

}
