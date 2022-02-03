package no.nav.kontantstotte.innlogging;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.ParseException;
import java.util.Objects;

public class InnloggingUtils {

    public static String hentFnrFraToken() {
        String bearerToken = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader("Authorization");
        String jwtToken = bearerToken.replace("Bearer ", "");
        try {
            JWT jwt = JWTParser.parse(jwtToken);
            return jwt.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new InnsynOppslagException("Kan ikke hente fnr fra token " + e);
        }
    }

}
