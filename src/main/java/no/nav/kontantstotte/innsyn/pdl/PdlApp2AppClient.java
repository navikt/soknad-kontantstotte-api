package no.nav.kontantstotte.innsyn.pdl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.familie.http.client.AbstractRestClient;
import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonBolk;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonBolkRequest;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonBolkRequestVariables;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonBolkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static no.nav.kontantstotte.innsyn.pdl.PdlUtils.HENT_PERSON_MED_BOLK_QUERY;
import static no.nav.kontantstotte.innsyn.pdl.PdlUtils.PATH_GRAPHQL;
import static no.nav.kontantstotte.innsyn.pdl.PdlUtils.httpHeaders;

@Service
public class PdlApp2AppClient extends AbstractRestClient {

    private static final Logger logger = LoggerFactory.getLogger(PdlApp2AppClient.class);

    private final URI pdlUrl;
    private final RestOperations restTemplate;
    private final ObjectMapper mapper;

    public PdlApp2AppClient(@Value("${PDL_URL}") URI pdlUrl,
                            @Qualifier("clientCredential") RestOperations restTemplate,
                            ObjectMapper mapper) {
        super(restTemplate, "pdl.personInfo");
        this.pdlUrl = UriComponentsBuilder.fromUri(pdlUrl).path(PATH_GRAPHQL).build().toUri();
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public List<PdlHentPersonBolk> hentPersonerMedBolk(List<String> identer) {
        PdlHentPersonBolkRequestVariables requestVariables = new PdlHentPersonBolkRequestVariables(identer);
        PdlHentPersonBolkRequest request = new PdlHentPersonBolkRequest(requestVariables, HENT_PERSON_MED_BOLK_QUERY);

        try {
            HttpEntity<PdlHentPersonBolkRequest> httpEntity = new HttpEntity<>(request, httpHeaders());
            var responseEntity = restTemplate.exchange(pdlUrl.toString(),
                                                       HttpMethod.POST,
                                                       httpEntity,
                                                       PdlHentPersonBolkResponse.class);
            var respons = Objects.requireNonNull(responseEntity.getBody(), "Fikk null respons fra PDL");
            if (!respons.harFeil()) {
                return respons.getData().getHentPersonBolk();
            }
            logger.warn("Respons fra PDL:{}", mapper.writeValueAsString(respons));
            throw new InnsynOppslagException("Feil ved oppslag på person:" + respons.errorMessages());
        } catch (RestClientException | JsonProcessingException e) {
            logger.warn("Fikk exception ved henting av person med bolk ", e);
            throw new InnsynOppslagException(e.getMessage());
        }
    }
}
