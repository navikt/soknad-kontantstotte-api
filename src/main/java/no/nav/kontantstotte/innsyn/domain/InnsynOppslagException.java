package no.nav.kontantstotte.innsyn.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InnsynOppslagException extends RuntimeException {
    public InnsynOppslagException(String msg) {
        super(msg);
    }
}
