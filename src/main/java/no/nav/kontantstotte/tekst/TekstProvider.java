package no.nav.kontantstotte.tekst;

import java.util.*;
import java.util.function.Function;

import static java.util.ResourceBundle.getBundle;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

public class TekstProvider {

    private final Map<String, Map<String, String>> properties;


    public TekstProvider(String bundleNavn, String... gyldigeSprak) {

        Function<ResourceBundle, Map<String, String>> bundleToMap = bundle -> bundle.keySet().stream()
                .collect(toMap(identity(), bundle::getString));

        properties = Arrays.stream(gyldigeSprak)
                .map(language -> getBundle(bundleNavn, new Locale(language), new Utf8Control()))
                .collect(toMap(bundle -> bundle.getLocale().getLanguage(), bundleToMap));

    }

    public Map<String, String> hentTekster(String sprak) {
        return properties.get(sprak);
    }


}
