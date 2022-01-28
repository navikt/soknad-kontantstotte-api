package no.nav.kontantstotte.api.rest;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import no.nav.security.token.support.core.api.ProtectedWithClaims;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.kontantstotte.api.rest.dto.BarnDto;
import no.nav.kontantstotte.innsyn.domain.InnsynService;


@RestController
@RequestMapping("api/barn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class BarnController {

    private static final DateTimeFormatter DATO_PATTERN_SOKNAD = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final InnsynService innsynServiceClient;

    public BarnController(InnsynService innsynServiceClient) {
        this.innsynServiceClient = innsynServiceClient;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BarnDto> hentBarnInfoOmSoker() {
        String fnr = hentFnrFraToken();
        return innsynServiceClient.hentBarnInfo(fnr)
                .stream()
                .map(barn -> new BarnDto(
                        barn.getFulltnavn(),
                        barn.getFødselsdato(),
                        barn.getFødselsnummer()))
                .collect(Collectors.toList());
    }
}
