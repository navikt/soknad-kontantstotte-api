package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TekstHelper {


    static Map<String, String> mockTekster(AbstractMap.SimpleEntry<String, String>... tekst) {
        return Collections.unmodifiableMap(Stream.of(tekst)
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
    }

    static AbstractMap.SimpleEntry<String, String> tekst(Tekstnokkel nokkel) {
        return new AbstractMap.SimpleEntry<>(nokkel.getNokkel(), nokkel.getNokkel());
    }

    static AbstractMap.SimpleEntry<String, String> tekst(Tekstnokkel nokkel, String tekstinnhold) {
        return new AbstractMap.SimpleEntry<>(nokkel.getNokkel(), tekstinnhold);
    }

}
