package no.nav.kontantstotte.innsending.oppsummering;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElementJsonDeserializer extends JsonDeserializer<Element> {

    @Override
    public Element deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext
    ) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.readValueAsTree();

        if (node.get("svar").isArray()) {
            return new Element<List<String>>(
                    node.get("sporsmal").isNull() ? null : node.get("sporsmal").asText(),
                    node.get("svar").isNull() ? null : new ObjectMapper().convertValue(node.get("svar"), ArrayList.class),
                    node.get("advarsel").isNull() ? null : node.get("advarsel").asText(),
                    node.get("underelementer").isNull() ? null : new ObjectMapper().convertValue(node.get("underelementer"), ArrayList.class)
            );
        } else if (node.get("svar").isTextual()) {
            return new Element<>(
                    node.get("sporsmal").isNull() ? null : node.get("sporsmal").asText(),
                    node.get("svar").isNull() ? null : node.get("svar").asText(),
                    node.get("advarsel").isNull() ? null : node.get("advarsel").asText(),
                    node.get("underelementer").isNull() ? null : new ObjectMapper().convertValue(node.get("underelementer"), ArrayList.class)
            );
        }

        return null;
    }
}
