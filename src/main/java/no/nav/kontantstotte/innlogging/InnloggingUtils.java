package no.nav.kontantstotte.innlogging;

import no.nav.security.token.support.spring.SpringTokenValidationContextHolder;

public class InnloggingUtils {

    public static String hentFnrFraToken() {
        return new SpringTokenValidationContextHolder().getTokenValidationContext()
                                                       .getClaims("selvbetjening")
                                                       .getSubject();
    }

}
