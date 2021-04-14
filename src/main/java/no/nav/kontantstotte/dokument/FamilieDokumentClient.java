package no.nav.kontantstotte.dokument;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.familie.kontrakter.felles.Ressurs;
import no.nav.familie.log.NavHttpHeaders;
import no.nav.familie.log.mdc.MDCConstants;
import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.client.TokenHelper;
import no.nav.kontantstotte.innsending.InnsendingException;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
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
        logger.info("get familie-dokument");
        HttpHeaders headers = lagerHttpHeadersMedBrukerToken();
        ResponseEntity<Ressurs> response = restTemplate.exchange(genererVedleggUri(vedleggsId), HttpMethod.GET, new HttpEntity<>(headers), Ressurs.class);
        logger.info(response.getStatusCode().toString());
        return response.getStatusCode().is2xxSuccessful() ? Base64.getDecoder().decode(response.getBody().getData().toString().getBytes(
                StandardCharsets.UTF_8)) : null;
    }

    public String lagreVedlegg(MultipartFile multipartFile) throws IOException {
        ByteArrayResource fileResource = new ByteArrayResource(multipartFile.getBytes()) {
            @Override
            public String getFilename() {
                return multipartFile.getOriginalFilename();
            }
        };
        HttpHeaders headers = lagerHttpHeadersMedBrukerToken();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", fileResource);
        HttpEntity<MultiValueMap> entity = new HttpEntity<>(body, headers);
        logger.info("post familie-dokument {}", familieDokumentVedleggUri);
        ResponseEntity<Map> response = restTemplate.postForEntity(familieDokumentVedleggUri, entity, Map.class);
        return response.getStatusCode().is2xxSuccessful() ? response.getBody().get("dokumentId").toString() : null;
    }

    private HttpHeaders lagerHttpHeadersMedBrukerToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                    TokenHelper.generateAuthorizationHeaderValueForLoggedInUser(contextHolder));
        return headers;
    }
}

