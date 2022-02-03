package no.nav.kontantstotte.config;

import no.nav.kontantstotte.innsyn.domain.FortroligAdresseException;
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException;
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

    @ExceptionHandler(value = {JwtTokenUnauthorizedException.class, HttpClientErrorException.Unauthorized.class})
    public ResponseEntity<String> handleUnauthorizedException() {
        logger.warn("Kan ikke behandle pga. bruker ikke logget inn.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Du er ikke logget inn");
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
