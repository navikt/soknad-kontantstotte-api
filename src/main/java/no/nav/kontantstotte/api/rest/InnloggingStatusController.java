package no.nav.kontantstotte.api.rest;


import no.nav.security.token.support.core.api.ProtectedWithClaims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class InnloggingStatusController {

    public InnloggingStatusController() {
    }

    /**
     * TODO Remove after frontend is updated
     *
     * @deprecated remove after frontend is updated
     */
    @GetMapping(path = "status/ping")
    @Deprecated
    public String ping() {
        return "pong";
    }

    @GetMapping(path = "verify/loggedin")
    public ResponseEntity verifyUserLoggedIn() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
