package no.nav.kontantstotte.dokument;

import no.nav.familie.kontrakter.felles.Ressurs;
import no.nav.kontantstotte.client.TokenHelper;
import no.nav.security.token.support.core.context.TokenValidationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Component
public class FamilieDokumentClient {

    private Logger logger = LoggerFactory.getLogger(FamilieDokumentClient.class);
    private static final Logger secureLogger = LoggerFactory.getLogger("secureLogger");

    private URI familieDokumentUri;
    private RestOperations restTemplate;
    private static String VEDLEGG_PATH = "/mapper/familievedlegg/";

    private URI familieDokumentVedleggUri;
    private final TokenValidationContextHolder contextHolder;

    public FamilieDokumentClient(@Value("${FAMILIE_DOKUMENT_API_URL}") URI uri,
                                 TokenValidationContextHolder contextHolder,
                                 @Qualifier("tokenExchange") RestOperations restTemplate) {
        this.familieDokumentUri = uri;
        this.contextHolder = contextHolder;
        this.restTemplate = restTemplate;
        this.familieDokumentVedleggUri = UriComponentsBuilder.fromUri(familieDokumentUri)
                                                             .path(VEDLEGG_PATH).build().toUri();
    }

    private URI genererVedleggUri(String vedleggsId) {
        return UriComponentsBuilder.fromUri(familieDokumentVedleggUri).path(vedleggsId).build().toUri();
    }

    public byte[] hentVedlegg(String vedleggsId) {
        HttpHeaders headers = lagerHttpHeadersMedBrukerToken();
        try {
            ResponseEntity<Ressurs> response =
                    restTemplate.exchange(genererVedleggUri(vedleggsId),
                                          HttpMethod.GET,
                                          new HttpEntity<>(headers),
                                          Ressurs.class);
            logger.info("Hent vedlegg fra familie-dokument: {}", response.getStatusCode().toString());
            return response.getStatusCode().is2xxSuccessful() ?
                    Base64.getDecoder().decode(Objects.requireNonNull(response.getBody()).getData().toString().getBytes(
                            StandardCharsets.UTF_8)) :
                    null;
        } catch (Exception e) {
            logger.warn("Feil med å hent vedlegg fra familie-dokument");
            return null;
        }
    }

    public String lagreVedlegg(byte[] vedleggData, String filenavn) {
        HttpHeaders headers = lagerHttpHeadersMedBrukerToken();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        ByteArrayResource fileResource = new ByteArrayResource(vedleggData) {
            @Override
            public String getFilename() {
                return filenavn;
            }
        };
        body.add("file", fileResource);
        HttpEntity<MultiValueMap> entity = new HttpEntity<>(body, headers);
        logger.info("post {}", familieDokumentVedleggUri);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(familieDokumentVedleggUri, entity, Map.class);
            logger.info("POST vedlegg til familie-dokument {}: {}",
                        familieDokumentVedleggUri,
                        response.getStatusCode().toString());
            return response.getStatusCode().is2xxSuccessful() ? response.getBody().get("dokumentId").toString() : null;
        } catch (Exception e) {
            logger.error("Feil med å lagre vedlegg til familie-dokument");
            secureLogger.error("Feil med å lagre vedlegg til familie-dokument", e);
            return null;
        }
    }

    private HttpHeaders lagerHttpHeadersMedBrukerToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                    TokenHelper.generateAuthorizationHeaderValueForLoggedInUser(contextHolder));
        return headers;
    }
}

