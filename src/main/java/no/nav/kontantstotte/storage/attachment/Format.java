package no.nav.kontantstotte.storage.attachment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

public enum Format {
    PDF("application/pdf"),
    PNG("image/jpeg"),
    JPG("image/png");

    private static final Logger log = LoggerFactory.getLogger(Format.class);

    public static Optional<Format> fromMimeType(String mimeType) {
        log.info("Forsøker å finne gydlig vedleggsformat fra detektert format {}", mimeType);
        return Arrays.stream(values()).filter(format -> format.mimeType.equalsIgnoreCase(mimeType)).findAny();
    }

    String mimeType;

    Format(String mimeType) {
        this.mimeType = mimeType;
    }
}
