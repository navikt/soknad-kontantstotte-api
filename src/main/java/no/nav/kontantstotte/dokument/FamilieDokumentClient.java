package no.nav.kontantstotte.dokument;

import no.nav.familie.kontrakter.felles.Ressurs;
import no.nav.kontantstotte.client.TokenHelper;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@Component
public class FamilieDokumentClient {

    private Logger logger = LoggerFactory.getLogger(FamilieDokumentClient.class);
    private URI familieDokumentUri;
    private RestOperations restTemplate;
    private static String VEDLEGG_PATH = "/mapper/familievedlegg/";

    private URI familieDokumentVedleggUri;
    private OIDCRequestContextHolder contextHolder;

    public FamilieDokumentClient(@Value("${FAMILIE_DOKUMENT_API_URL}") URI uri,
                                 RestOperations restTemplate,
                                 OIDCRequestContextHolder contextHolder) {
        this.familieDokumentUri = uri;
        this.restTemplate = restTemplate;
        this.familieDokumentVedleggUri = UriComponentsBuilder.fromUri(familieDokumentUri)
                                                             .path(VEDLEGG_PATH).build().toUri();
        this.contextHolder = contextHolder;
    }

    private URI genererVedleggUri(String vedleggsId) {
        return UriComponentsBuilder.fromUri(familieDokumentVedleggUri).path(vedleggsId).build().toUri();
    }

    public byte[] hentVedlegg(String vedleggsId) {
        HttpHeaders headers = lagerHttpHeadersMedBrukerToken();
        ResponseEntity<Ressurs> response =
                restTemplate.exchange(genererVedleggUri(vedleggsId), HttpMethod.GET, new HttpEntity<>(headers), Ressurs.class);
        logger.info("Hent vedlegg fra familie-dokument: {}", response.getStatusCode().toString());
        return response.getStatusCode().is2xxSuccessful() ?
                Base64.getDecoder().decode(response.getBody().getData().toString().getBytes(
                        StandardCharsets.UTF_8)) :
                null;
    }

    public String lagreVedlegg(MultipartFile multipartFile) {
        HttpHeaders headers = lagerHttpHeadersMedBrukerToken();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        try {
            ByteArrayResource fileResource = new ByteArrayResource(multipartFile.getBytes()) {
                @Override
                public String getFilename() {
                    return multipartFile.getOriginalFilename();
                }
            };
            body.add("file", fileResource);
        } catch (IOException e) {
            logger.error("Ugyldige vedleggsdata");
            return null;
        }

        HttpEntity<MultiValueMap> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(familieDokumentVedleggUri, entity, Map.class);
        logger.info("POST vedlegg til familie-dokument {}: {}", familieDokumentVedleggUri, response.getStatusCode().toString());
        return response.getStatusCode().is2xxSuccessful() ? response.getBody().get("dokumentId").toString() : null;
    }

    private HttpHeaders lagerHttpHeadersMedBrukerToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                    TokenHelper.generateAuthorizationHeaderValueForLoggedInUser(contextHolder));
        return headers;
    }
}

