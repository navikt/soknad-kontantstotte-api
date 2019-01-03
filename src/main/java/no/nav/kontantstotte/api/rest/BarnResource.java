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
import java.util.*;
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
            return genererReturBarn(sokerBarn);
        } else {
            return new ArrayList<>();
        }
    }

    public static Set<Set<BarnDto>> powerSet(List<BarnDto> barnList) {
        Set<Set<BarnDto>> alleSet = new HashSet<>();
        if (barnList.isEmpty()) {
            alleSet.add(new HashSet<>());
            return alleSet;
        }
        else {
            BarnDto head = barnList.get(0);
            List<BarnDto> rest = new ArrayList<>(barnList.subList(1, barnList.size()));
            for (Set<BarnDto> set : powerSet(rest)) {
                Set<BarnDto> nyttSet = new HashSet<>();
                nyttSet.add(head);
                nyttSet.addAll(set);
                alleSet.add(nyttSet);
                alleSet.add(set);
            }
            return alleSet;
        }
    }

    private static List<BarnDto> genererReturBarn(List<BarnDto> alleBarn) {
        List<BarnDto> returBarn = new ArrayList<>(alleBarn);
        ArrayList<Set<BarnDto>> alleFlerlinger = finnFlerlinger(alleBarn);
        for (Set<BarnDto> flerlingSubset : alleFlerlinger) {
            if (!flerlingErSubset(flerlingSubset, alleFlerlinger)) {
                returBarn.add(genererFlerlingBarn(flerlingSubset));
            }
        }
        return returBarn;
    }

    private static ArrayList<Set<BarnDto>> finnFlerlinger(List<BarnDto> alleBarn) {
        ArrayList<Set<BarnDto>> flerlinger = new ArrayList<>();
        for (Set<BarnDto> set : powerSet(alleBarn)) {
            if (set.size() > 1) {
                List<LocalDate> fdatoer = set
                        .stream()
                        .map( barn -> LocalDate.parse(barn.getFodselsdato(), DATO_PATTERN_SOKNAD))
                        .collect(Collectors.toList());
                if (datoerInnenforIntervall(Collections.min(fdatoer), Collections.max(fdatoer))) {
                    flerlinger.add(set);
                }
            }
        }
        return flerlinger;
    }

    private static boolean flerlingErSubset(Set<BarnDto> flerlingSubset, ArrayList<Set<BarnDto>> alleFlerlinger) {
        ArrayList<Set<BarnDto>> rest = new ArrayList<>(alleFlerlinger);
        rest.remove(flerlingSubset);
        for (Set<BarnDto> flerlingSet : rest) {
            if (flerlingSet.containsAll(flerlingSubset)) {
                return true;
            }
        }
        return false;
    }

    private static BarnDto genererFlerlingBarn(Set<BarnDto> flerlinger) {
        List<String> navnUtenSlektsnavn = new ArrayList<>();
        List<String> fodselsdatoer = new ArrayList<>();
        flerlinger.forEach( flerling -> {
            navnUtenSlektsnavn.add(flerling.getFulltnavn().replaceAll(".*? ", ""));
            fodselsdatoer.add(flerling.getFodselsdato());
        });
        return new BarnDto(
                joinAttributter(navnUtenSlektsnavn, flerlinger.size()),
                joinAttributter(fodselsdatoer, flerlinger.size()),
                true);
    }

    private static String joinAttributter(List<String> attributter, int antallFlerlinger) {
        return antallFlerlinger > 2
                ? String.join(", ", attributter.subList(0, attributter.size() - 1)) + " og " + attributter.get(attributter.size() - 1)
                : String.join(" og ", attributter);
    }

    private static boolean datoerInnenforIntervall(LocalDate dato1, LocalDate dato2) {
        Period diff = Period.between(dato1, dato2);
        return diff.getYears() == 0 && diff.getMonths() == 0 && diff.getDays() <= 3;
    }
}
