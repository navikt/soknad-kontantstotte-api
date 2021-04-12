package no.nav.kontantstotte.dokument;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.familie.kontrakter.felles.Ressurs;
import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.client.TokenHelper;
import no.nav.kontantstotte.innsending.InnsendingException;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private OIDCRequestContextHolder contextHolder;
    private HttpClient client;
    private ObjectMapper objectMapper;

    public FamilieDokumentClient(@Value("${FAMILIE_DOKUMENT_API_URL}") URI uri,
                                 @Qualifier("restKlientBearerToken") RestOperations restTemplate,
                                 OIDCRequestContextHolder contextHolder,
                                 ObjectMapper objectMapper) {
        this.familieDokumentUri = uri;
        this.restTemplate = restTemplate;
        this.familieDokumentVedleggUri = UriComponentsBuilder.fromUri(familieDokumentUri)
                                                             .path(VEDLEGG_PATH).build().toUri();
        this.contextHolder = contextHolder;
        this.client = HttpClientUtil.create();
        this.objectMapper = objectMapper;
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

    public String lagreVedlegg(MultipartFile multipartFile) throws IOException {
        ByteArrayResource fileResource = new ByteArrayResource(multipartFile.getBytes()){
            @Override
            public String getFilename() {
                return multipartFile.getOriginalFilename();
            }
        };
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(TokenHelper.generateAuthorizationHeaderValueForLoggedInUser(contextHolder));
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", fileResource);
        HttpEntity<MultiValueMap> entity = new HttpEntity<>(body, headers);
        logger.info("post familie-dokument {}", familieDokumentVedleggUri);
        ResponseEntity<Map> response = restTemplate.postForEntity(familieDokumentVedleggUri, entity, Map.class);
        return response.getStatusCode().is2xxSuccessful()? response.getBody().get("dokumentId").toString(): null;
        /*
        HttpRequest lagreVedleggRequest = HttpClientUtil.createRequest(TokenHelper.generateAuthorizationHeaderValueForLoggedInUser(contextHolder))
                      .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.MULTIPART_FORM_DATA_VALUE)
                      .header(HttpHeader.ACCEPT.asString(), MediaType.APPLICATION_JSON_VALUE)
                      .uri(familieDokumentVedleggUri)
                      .POST(HttpRequest.BodyPublishers.ofByteArray(multipartFile.getBytes()))
                      .build();
        HttpResponse<String> response = sendRequest(lagreVedleggRequest);
        Map<String, String> bodyData = objectMapper.readValue(response.body(), Map.class);
//        ResponseEntity<Map> response = restTemplate.postForEntity(familieDokumentVedleggUri,
//                                                                  entity, Map.class);
//        logger.info(response.getStatusCode().toString());
        return bodyData.get("dokumentId");
         */
    }

    private HttpResponse sendRequest(HttpRequest familieDokumentRequest) {
        try {
            HttpResponse<String> response = client.send(familieDokumentRequest, HttpResponse.BodyHandlers.ofString());

            if (!HttpStatus.Series.SUCCESSFUL.equals(HttpStatus.Series.resolve(response.statusCode()))) {
                logger.warn("Kontakt familie-dokument feil: {}", response.statusCode());
                throw new InternalError(
                        "Response fra familie-dokument: " + response.statusCode());
            }
            return response;
        } catch (IOException | InterruptedException e) {
            logger.warn("Feilet ved kontakt med familie-dokument: {}", e.getMessage());
            throw new InternalError("Feilet ved kontakt med familie-dokument");
        }
    }
}

