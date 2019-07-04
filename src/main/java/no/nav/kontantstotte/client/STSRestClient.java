package no.nav.kontantstotte.client;

import static java.time.LocalDate.now;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class STSRestClient {
    private ObjectMapper mapper;

    private HttpClient client;
    private URI stsUrl;
    private String stsUsername;
    private String stsPassword;

    private AccessTokenResponse cachedToken;

    public STSRestClient(ObjectMapper objectMapper, @Value("${STS_URL}") URI stsUrl, @Value("${CREDENTIAL_USERNAME}") String stsUsername, @Value("${CREDENTIAL_PASSWORD}") String stsPassword) {
        this.mapper = objectMapper;
        this.client = HttpClientUtil.create();
        this.stsUrl = URI.create(stsUrl + "/rest/v1/sts/token?grant_type=client_credentials&scope=openid");
        this.stsUsername = stsUsername;
        this.stsPassword = stsPassword;
    }

    private static String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    private boolean hasTokenExpired() {
        if (cachedToken == null) {
            return true;
        }
        return Instant.ofEpochMilli(cachedToken.getExpires_in())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .minus(10, ChronoUnit.MINUTES)
                .isAfter(now());
    }

    public String getSystemOIDCToken() {
        if (!hasTokenExpired()) {
            return cachedToken.getAccess_token();
        }

        HttpRequest request = HttpClientUtil.createRequest(basicAuth(stsUsername, stsPassword))
                .uri(stsUrl)
                .header("Content-Type", "application/json")
                .build();

        AccessTokenResponse accessTokenResponse;
        try {
            accessTokenResponse = client
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(it -> {
                        try {
                            return mapper.readValue(it, AccessTokenResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "";
        }

        if (accessTokenResponse != null) {
            this.cachedToken = accessTokenResponse;
            return accessTokenResponse.getAccess_token();
        } else {
            return "";
        }
    }
}