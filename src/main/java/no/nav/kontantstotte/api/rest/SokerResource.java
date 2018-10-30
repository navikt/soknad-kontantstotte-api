package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.api.rest.dto.SokerDto;
import no.nav.kontantstotte.person.domain.Person;
import no.nav.kontantstotte.person.domain.PersonService;
import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.jaxrs.OidcRequestContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("soker")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class SokerResource {

    private static final String SELVBETJENING = "selvbetjening";

    private final PersonService personService;

    @Inject
    public SokerResource(PersonService personService) {
        this.personService = personService;
    }

    @GET
    public SokerDto hentInfoOmSoker() {
        Person person = personService.hentPersonInfo(hentFnrFraToken());
        return new SokerDto(hentFnrFraToken(), person.getFornavn(), person.getNavn());
    }

    public static String hentFnrFraToken() {
        OIDCValidationContext context = OidcRequestContext.getHolder().getOIDCValidationContext();
        return context.getClaims(SELVBETJENING).getClaimSet().getSubject();
    }
}
