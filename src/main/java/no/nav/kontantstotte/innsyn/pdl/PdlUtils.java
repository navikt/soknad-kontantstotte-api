package no.nav.kontantstotte.innsyn.pdl;

import no.nav.familie.kontrakter.felles.Tema;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class PdlUtils {

    public static final String PATH_GRAPHQL = "graphql";

    public static final String HENT_ENKEL_PERSON_QUERY = graphqlQuery("hentperson-enkel");
    public static final String HENT_PERSON_MED_RELASJONER_QUERY = graphqlQuery("hentperson-relasjoner");
    public static final String HENT_PERSON_MED_BOLK_QUERY = graphqlQuery("hentperson-bolk");

    public static String graphqlQuery(String pdlResource) {
        String query;
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

    public static HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.add("Tema", Tema.KON.name());
        return httpHeaders;
    }

}
