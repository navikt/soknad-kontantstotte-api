package no.nav.kontantstotte.dokument;

import no.nav.familie.kontrakter.felles.Ressurs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class FamilieDokumentClient {

    private Logger logger= LoggerFactory.getLogger(FamilieDokumentClient.class);
    private URI familieDokumentUri;
    private RestOperations restTemplate;
    private static String VEDLEGG_PATH = "/mapper/familievedlegg/";

    private URI familieDokumentVedleggUri;

    public FamilieDokumentClient(@Value("${FAMILIE_DOKUMENT_API_URL}") URI uri,
                                 @Qualifier("restKlientBearerToken") RestOperations restTemplate) {
        this.familieDokumentUri = uri;
        this.restTemplate = restTemplate;
        this.familieDokumentVedleggUri = UriComponentsBuilder.fromUri(familieDokumentUri)
                                                             .path(VEDLEGG_PATH).build().toUri();
    }

    private URI genererVedleggUri(String vedleggsId) {
        return UriComponentsBuilder.fromUri(familieDokumentVedleggUri).path(vedleggsId).build().toUri();
    }

    public byte[] hentVedlegg(String vedleggsId) {
        logger.info("get familie-dokument");
        ResponseEntity<Ressurs> response = restTemplate.getForEntity(genererVedleggUri(vedleggsId), Ressurs.class);
        logger.info(response.getStatusCode().toString());
        return response.getStatusCode().is2xxSuccessful() ? (byte[]) response.getBody().getData() : null;
    }

    public String lagreVedlegg(MultipartFile multipartFile) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Map<String, Object> body = new HashMap<>();
        body.put("file", multipartFile);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        logger.info("post familie-dokument");
        ResponseEntity<Map> response = restTemplate.postForEntity(familieDokumentVedleggUri,
                                                                  entity, Map.class);
        logger.info(response.getStatusCode().toString());
        return response.getStatusCode().is2xxSuccessful() ? (String) response.getBody().get("dokumentId") : null;
    }
}

