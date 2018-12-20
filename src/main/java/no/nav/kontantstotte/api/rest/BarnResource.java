package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.api.rest.dto.BarnDto;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.innsyn.domain.IInnsynServiceClient;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.BRUK_TPS_INTEGRASJON;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;


@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("barn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class BarnResource {

    private static final DateTimeFormatter DATO_PATTERN_SOKNAD = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final IInnsynServiceClient innsynServiceClient;

    @Inject
    public BarnResource(IInnsynServiceClient innsynServiceClient) {
        this.innsynServiceClient = innsynServiceClient;
    }

    @GET
    public List<BarnDto> hentBarnInfoOmSoker() {
        String fnr = hentFnrFraToken();
        if(UnleashProvider.get().isEnabled(BRUK_TPS_INTEGRASJON)) {
            List<BarnDto> sokerBarn = innsynServiceClient.hentBarnInfo(fnr)
                    .stream()
                    .map(barn -> new BarnDto(
                            barn.getFulltnavn(),
                            barn.getFodselsdato(),
                            false))
                    .collect(Collectors.toList());
            sokerBarn.addAll(finnFlerlinger(sokerBarn));
            return sokerBarn;
        } else {
            return new ArrayList<>();
        }
    }

    private List<BarnDto> finnFlerlinger(List<BarnDto> barn) {
        List<BarnDto> flerlinger = new ArrayList<>();
        for (int i = 0; i < barn.size(); i++) {
            for (int k = i + 1; k < barn.size(); k++) {
                String fodselsdato1 = barn.get(i).getFodselsdato();
                String fodselsdato2 = barn.get(k).getFodselsdato();
                if (datoerInnenforIntervall(fodselsdato1, fodselsdato2)) {
                    String kombinertNavn = kombinerNavn(barn.get(i).getFulltnavn(), barn.get(k).getFulltnavn());
                    String kombinertFodselsdatoer = fodselsdato1.equals(fodselsdato2)
                            ? fodselsdato1
                            : fodselsdato1 + " og " + fodselsdato2;
                    flerlinger.add(new BarnDto(kombinertNavn, kombinertFodselsdatoer, true));
                }
            }
        }
        return flerlinger;
    }

    private String kombinerNavn(String fulltnavn1, String fulltnavn2) {
        String utenSlektsnavn1 = fulltnavn1.replaceAll(".*? ", "");
        String utenSlektsnavn2 = fulltnavn2.replaceAll(".*? ", "");
        return utenSlektsnavn1 + " og " + utenSlektsnavn2;
    }

    private boolean datoerInnenforIntervall(String dato1, String dato2) {
        Period diff = Period.between(LocalDate.parse(dato1, DATO_PATTERN_SOKNAD), LocalDate.parse(dato2, DATO_PATTERN_SOKNAD));
        return diff.getDays() <= 3;
    }
}
