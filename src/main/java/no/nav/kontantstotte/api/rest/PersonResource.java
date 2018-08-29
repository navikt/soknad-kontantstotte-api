package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.service.PersonService;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("person")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class PersonResource {

    @Value("${apikeys.key:x-nav-apiKey}")
    private String key;

    @Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}")
    private URI proxyServiceUri;

    @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}")
    private String proxyApiKey;

    private final PersonService personService;

    @Inject
    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @GET
    public String hentPerson() {
        return personService.hentPerson();
    }
}
