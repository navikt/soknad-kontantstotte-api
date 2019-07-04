package no.nav.kontantstotte.api.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.kontantstotte.api.rest.dto.BarnDto;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.security.oidc.api.ProtectedWithClaims;


@RestController
@RequestMapping("api/barn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class BarnResource {

    private static final DateTimeFormatter DATO_PATTERN_SOKNAD = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final InnsynService innsynServiceClient;

    public BarnResource(InnsynService innsynServiceClient) {
        this.innsynServiceClient = innsynServiceClient;
    }

    @GetMapping(produces = APPLICATION_JSON)
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
