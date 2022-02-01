package no.nav.kontantstotte.innsyn.pdl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.familie.http.client.AbstractPingableRestClient;
import no.nav.familie.kontrakter.felles.Tema;
import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlForelderBarnRelasjon;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonBolk;
import no.nav.kontantstotte.innsyn.pdl.domene.PdlHentPersonBolkResponse;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PdlClient extends AbstractPingableRestClient {

    private static final Logger logger = LoggerFactory.getLogger(PdlClient.class);

    private static final String PATH_GRAPHQL = "graphql";
    public static final String HENT_ENKEL_PERSON_QUERY = graphqlQuery("hentperson-enkel");
    public static final String HENT_PERSON_MED_RELASJONER_QUERY = graphqlQuery("hentperson-relasjoner");
    public static final String HENT_PERSON_MED_BOLK_QUERY = graphqlQuery("hentperson-bolk");

    private final URI pdlUrl;
    private final RestOperations restTemplate;
    private ObjectMapper mapper;

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
        PdlPersonRequestVariables<String> requestVariables = new PdlPersonRequestVariables<>(ident);
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
        PdlPersonRequestVariables<String> requestVariables = new PdlPersonRequestVariables<>(ident);
        PdlPersonRequest request = new PdlPersonRequest(requestVariables, HENT_PERSON_MED_RELASJONER_QUERY);

        try {
            var respons = getRespons(request);
            //TODO midlertidig endringer,logg må flyttes til linje 120
            logger.warn("Respons fra PDL:{}", mapper.writeValueAsString(respons));
            if (!respons.harFeil()) {
                return respons.getData().getPerson().getForelderBarnRelasjon();
            }
            throw new InnsynOppslagException("Feil ved oppslag på person:" + respons.errorMessages());
        } catch (RestClientException | JsonProcessingException e) {
            logger.warn("Fikk exception ved henting av person med relasjoner ", e);
            throw new InnsynOppslagException(e.getMessage());
        }
    }

    public List<PdlHentPersonBolk> hentPersonerMedBolk(List<String> ident) {
        PdlPersonRequestVariables<List<String>> requestVariables = new PdlPersonRequestVariables<>(ident);
        PdlPersonRequest request = new PdlPersonRequest(requestVariables, HENT_PERSON_MED_BOLK_QUERY);

        try {
            HttpEntity<PdlPersonRequest> httpEntity = new HttpEntity<>(request, httpHeaders());
            var responseEntity = restTemplate.exchange(getPingUrl(),
                                                       HttpMethod.POST,
                                                       httpEntity,
                                                       PdlHentPersonBolkResponse.class);
            var respons = Objects.requireNonNull(responseEntity.getBody(), "Fikk null respons fra PDL");
            ;
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

    private HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.add("Tema", Tema.KON.name());
        return httpHeaders;
    }

    private static String graphqlQuery(String pdlResource) {
        String query = "";
        ClassPathResource resource = new ClassPathResource("/pdl/" + pdlResource + ".graphql");
        try (InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            query = reader.lines()
                          .map(String::strip)
                          .collect(Collectors.joining(" "));
        } catch (IOException e) {
            throw new RuntimeException("Fil " + pdlResource + " kan ikke leses. Fikk feilmelding ", e);
        }
        return query;
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
