package no.nav.kontantstotte.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class TjenesteLogger {
    private static final Logger secureLogger = LoggerFactory.getLogger("secureLogger");

    public static void logTjenestekall(URI uri, String fnr, Object data) {
        secureLogger.info("[{}, {}]: {}", uri, fnr, data.toString());
    }

    public static void logFeil(URI uri, String fnr, Exception error) {
        secureLogger.error("[{}, {}]: {}", uri, fnr, error);
    }
}
