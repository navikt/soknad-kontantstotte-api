package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.api.rest.dto.BarnDto;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;


@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("barn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class BarnResource {

    private static final DateTimeFormatter DATO_PATTERN_SOKNAD = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final InnsynService innsynServiceClient;

    @Inject
    public BarnResource(InnsynService innsynServiceClient) {
        this.innsynServiceClient = innsynServiceClient;
    }

    @GET
    public List<BarnDto> hentBarnInfoOmSoker() {
        String fnr = hentFnrFraToken();
        return innsynServiceClient.hentBarnInfo(fnr)
                .stream()
                .map(barn -> new BarnDto(
                        barn.getFulltnavn(),
                        barn.getFodselsdato()))
                .collect(Collectors.toList());
    }
}
