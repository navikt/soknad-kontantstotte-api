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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
            sokerBarn.add(finnFlerlinger(sokerBarn));
            return sokerBarn;
        } else {
            return new ArrayList<>();
        }
    }

    private BarnDto finnFlerlinger(List<BarnDto> alleBarn) {
        Set<BarnDto> flerlinger = new HashSet<>();
        for (int i = 0; i < alleBarn.size(); i++) {
            for (int k = i + 1; k < alleBarn.size(); k++) {
                if (datoerInnenforIntervall(alleBarn.get(i).getFodselsdato(), alleBarn.get(k).getFodselsdato())) {
                    flerlinger.add(alleBarn.get(i));
                    flerlinger.add(alleBarn.get(k));
                }
            }
        }
        List<String> utenSlektsnavn = new ArrayList<>();
        List<String> fodselsdatoer = new ArrayList<>();
        flerlinger.forEach( flerling -> {
                utenSlektsnavn.add(flerling.getFulltnavn().replaceAll(".*? ", ""));
                fodselsdatoer.add(flerling.getFodselsdato());
        });

        String joinetNavn;
        String joinetFodselsdatoer;
        if (flerlinger.size() > 2) {
            joinetNavn = String.join(", ", utenSlektsnavn.subList(0,utenSlektsnavn.size() - 1)) + " og " + utenSlektsnavn.get(utenSlektsnavn.size() - 1);
            joinetFodselsdatoer = String.join(", ", fodselsdatoer.subList(0,fodselsdatoer.size() - 1)) + " og " + fodselsdatoer.get(fodselsdatoer.size() - 1);
        }
        else {
            joinetNavn = String.join(" og ", utenSlektsnavn);
            joinetFodselsdatoer = String.join(" og ", fodselsdatoer);
        }
        return new BarnDto(joinetNavn, joinetFodselsdatoer, true);
    }

    private boolean datoerInnenforIntervall(String dato1, String dato2) {
        Period diff = Period.between(LocalDate.parse(dato1, DATO_PATTERN_SOKNAD), LocalDate.parse(dato2, DATO_PATTERN_SOKNAD));
        return diff.getDays() <= 3;
    }
}
