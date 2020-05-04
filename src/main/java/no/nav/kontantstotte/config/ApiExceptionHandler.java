package no.nav.kontantstotte.config;

import no.nav.kontantstotte.innsyn.domain.FortroligAdresseException;
import no.nav.kontantstotte.storage.attachment.AttachmentConversionException;
import no.nav.security.spring.oidc.validation.interceptor.OIDCUnauthorizedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger secureLogger = LoggerFactory.getLogger("secureLogger");
    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler({FortroligAdresseException.class})
    public ResponseEntity<String> handleFortroligAdresseException() {
        return forbidden(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({OIDCUnauthorizedException.class, HttpClientErrorException.Unauthorized.class})
    public ResponseEntity<String> handleUnauthorizedException() {
        logger.warn("Kan ikke behandle pga. bruker ikke logget inn.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Du er ikke logget inn");
    }

    @ExceptionHandler({AttachmentConversionException.class})
    public ResponseEntity<String> handleAttachmentConversionException(AttachmentConversionException e) {
        logger.error("Kan ikke konvertere vedlegg : {}", ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Feil ved mottak av vedlegg");
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleException(Exception e) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity<String> error(HttpStatus status, Exception e) {
        secureLogger.error("Exception : ", e);
        logger.error("Exception : {}", ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(status).body("Feilen er logget vi ser på problemet!");
    }

    private ResponseEntity<String> forbidden(HttpStatus status) {
        logger.info("Kan ikke behandle pga. fortrolig adresse");
        return ResponseEntity.status(status).body("Kan ikke søke om kontantstøtte");
    }
}
