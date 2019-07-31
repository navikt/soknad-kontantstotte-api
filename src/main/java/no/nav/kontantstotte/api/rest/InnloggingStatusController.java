package no.nav.kontantstotte.api.rest;

import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.oidc.api.ProtectedWithClaims;

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
    public Response verifyUserLoggedIn() {
        return Response.ok().build();
    }
}