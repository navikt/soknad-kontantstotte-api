package no.nav.kontantstotte.innsyn.pdl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.familie.http.client.AbstractPingableRestClient;
import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlForelderBarnRelasjon;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonResponse;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlPersonData;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlPersonRequest;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlPersonRequestVariables;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static no.nav.kontantstotte.innsyn.pdl.PdlUtils.HENT_ENKEL_PERSON_QUERY;
import static no.nav.kontantstotte.innsyn.pdl.PdlUtils.HENT_PERSON_MED_RELASJONER_QUERY;
import static no.nav.kontantstotte.innsyn.pdl.PdlUtils.PATH_GRAPHQL;
import static no.nav.kontantstotte.innsyn.pdl.PdlUtils.graphqlQuery;
import static no.nav.kontantstotte.innsyn.pdl.PdlUtils.httpHeaders;

@Service
public class PdlClient extends AbstractPingableRestClient {

    private static final Logger logger = LoggerFactory.getLogger(PdlClient.class);

    private final URI pdlUrl;
    private final RestOperations restTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public PdlClient(@Value("${PDL_URL}") URI pdlUrl,
                     @Qualifier("tokenExchange") RestOperations restTemplate,
                     ObjectMapper mapper) {
        super(restTemplate, "pdl.personInfo");
        this.pdlUrl = UriComponentsBuilder.fromUri(pdlUrl).path(PATH_GRAPHQL).build().toUri();
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }


    @NotNull
    @Override
    public URI getPingUri() {
        return pdlUrl;
    }

    public String getPingUrl() {
        return pdlUrl.toString();
    }

    @Override
    public void ping() {
        try {
            getOperations().optionsForAllow(getPingUri());
        } catch (RestClientException e) {
            logger.warn("Kan ikke oppnå PDL, feilMelding=", e);
            throw new InnsynOppslagException(e.getMessage());
        }
    }

    public PdlPersonData hentPersoninfo(String ident) {
        PdlPersonRequestVariables requestVariables = new PdlPersonRequestVariables(ident);
        PdlPersonRequest request = new PdlPersonRequest(requestVariables, HENT_ENKEL_PERSON_QUERY);

        try {
            var respons = getRespons(request);
            if (!respons.harFeil()) {
                return respons.getData().getPerson();
            }
            logger.warn("Respons fra PDL:{}", mapper.writeValueAsString(respons));
            throw new InnsynOppslagException("Feil ved oppslag på person:" + respons.errorMessages());
        } catch (RestClientException | JsonProcessingException e) {
            logger.warn("Fikk exception ved henting av person ", e);
            throw new InnsynOppslagException(e.getMessage());
        }
    }

    public List<PdlForelderBarnRelasjon> hentPersoninfoMedRelasjoner(String ident) {
        PdlPersonRequestVariables requestVariables = new PdlPersonRequestVariables(ident);
        PdlPersonRequest request = new PdlPersonRequest(requestVariables, HENT_PERSON_MED_RELASJONER_QUERY);

        try {
            var respons = getRespons(request);
            if (!respons.harFeil()) {
                return respons.getData().getPerson().getForelderBarnRelasjon();
            }
            logger.warn("Respons fra PDL:{}", mapper.writeValueAsString(respons));
            throw new InnsynOppslagException("Feil ved oppslag på person:" + respons.errorMessages());
        } catch (RestClientException | JsonProcessingException e) {
            logger.warn("Fikk exception ved henting av person med relasjoner ", e);
            throw new InnsynOppslagException(e.getMessage());
        }
    }


    private PdlHentPersonResponse getRespons(PdlPersonRequest request) {
        HttpEntity<PdlPersonRequest> httpEntity = new HttpEntity<>(request, httpHeaders());
        var responseEntity = restTemplate.exchange(getPingUrl(),
                                                   HttpMethod.POST,
                                                   httpEntity,
                                                   PdlHentPersonResponse.class);
        return Objects.requireNonNull(responseEntity.getBody(), "Fikk null respons fra PDL");
    }

}
