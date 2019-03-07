package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.api.rest.dto.SokerDto;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.kontantstotte.metrics.MetricService;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.springframework.stereotype.Component;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;


@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("soker")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class SokerResource {

    private final InnsynService innsynServiceClient;
    private final MetricService metricService;

    @Inject
    public SokerResource(InnsynService innsynServiceClient, MetricService metricService) {
        this.innsynServiceClient = innsynServiceClient;
        this.metricService = metricService;
    }

    @GET
    public SokerDto hentPersonInfoOmSoker() {
        String fnr = hentFnrFraToken();

        Person person = innsynServiceClient.hentPersonInfo(fnr);
        metricService.getSoknadApnet().inc();
        return new SokerDto(fnr, person.getFornavn(), person.getFulltnavn(), person.getStatsborgerskap());
    }
}
