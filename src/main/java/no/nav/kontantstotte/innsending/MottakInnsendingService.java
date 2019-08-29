package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.client.TokenHelper;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class MottakInnsendingService implements InnsendingService {

    private final String kontantstotteMottakApiKeyUsername;
    private final String kontantstotteMottakApiKeyPassword;
    private URI mottakServiceUri;
    private OIDCRequestContextHolder contextHolder;
    private ObjectMapper mapper;
    private HttpClient client;
    private static final Logger LOG = LoggerFactory.getLogger(MottakInnsendingService.class);

    public MottakInnsendingService(@Value("${FAMILIE_KS_MOTTAK_API_URL}") URI mottakServiceUri,
                            @Value("${SOKNAD_KONTANTSTOTTE_API_FAMILIE_KS_MOTTAK_APIKEY_USERNAME}") String kontantstotteMottakApiKeyUsername,
                            @Value("${SOKNAD_KONTANTSTOTTE_API_FAMILIE_KS_MOTTAK_APIKEY_PASSWORD}") String kontantstotteMottakApiKeyPassword,
                            OIDCRequestContextHolder contextHolder,
                            ObjectMapper mapper) {

        this.kontantstotteMottakApiKeyUsername = kontantstotteMottakApiKeyUsername;
        this.kontantstotteMottakApiKeyPassword = kontantstotteMottakApiKeyPassword;
        this.mottakServiceUri = mottakServiceUri;
        this.contextHolder = contextHolder;
        this.mapper = mapper;
        this.client = HttpClientUtil.create();
    }


    @Override
    public Soknad sendInnSoknad(Soknad soknad) {
        LOG.info("Prøver å sende søknad til mottaket");
        try {
            HttpRequest mottakRequest = HttpClientUtil.createRequest(TokenHelper.generateAuthorizationHeaderValueForLoggedInUser(contextHolder))
                    .header(kontantstotteMottakApiKeyUsername, kontantstotteMottakApiKeyPassword)
                    .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.APPLICATION_JSON_VALUE)
                    .uri(URI.create(mottakServiceUri + "soknad"))
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(soknad)))
                    .build();

            try {
                HttpResponse<String> mottakresponse = client.send(mottakRequest, HttpResponse.BodyHandlers.ofString());
                LOG.info("Søknad sendt til mottaket. Response status: {}, respons: {}", mottakresponse.statusCode(), mottakresponse.body());
            } catch (IOException | InterruptedException e) {
                LOG.warn("Feilet under sending av søknad til mottak: {}", e.getMessage());
            }
        } catch (JsonProcessingException e) {
            throw new InnsendingException("Feiler under konvertering av innsending til json.");
        }
        return soknad;
    }
}
