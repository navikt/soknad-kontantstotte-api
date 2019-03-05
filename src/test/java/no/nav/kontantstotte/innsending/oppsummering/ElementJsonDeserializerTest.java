package no.nav.kontantstotte.innsending.oppsummering;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ElementJsonDeserializerTest {

    private ObjectMapper mapper;
    private ElementJsonDeserializer deserializer;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        deserializer = new ElementJsonDeserializer();
    }

    @Test
    public void klarer_aa_deserializere_element_med_string() throws IOException {
        String json = "{\n" +
                "          \"sporsmal\": \"Sporsmal?\",\n" +
                "          \"svar\": \"Nei\",\n" +
                "          \"advarsel\": \"ADVARSEL!\",\n" +
                "          \"underelementer\": null\n" +
                "        }";

        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Element element = deserializer.deserialize(parser, ctxt);
        assertEquals(element.sporsmal, "Sporsmal?");
        assertEquals(element.svar, "Nei");
        assertEquals(element.advarsel, "ADVARSEL!");
        assertEquals(element.underelementer, null);
    }

    @Test
    public void klarer_aa_deserializere_element_med_liste() throws IOException {

        String json = "{\n" +
                "          \"sporsmal\": \"Vedlegg?\",\n" +
                "          \"svar\": [\"Vedlegg1.png\", \"Vedlegg2.png\"],\n" +
                "          \"advarsel\": null,\n" +
                "          \"underelementer\": null\n" +
                "        }";

        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Element<List<String>> element = (Element) deserializer.deserialize(parser, ctxt);
        assertEquals(element.sporsmal, "Vedlegg?");
        assertEquals(element.svar.get(0), "Vedlegg1.png");
        assertEquals(element.svar.get(1), "Vedlegg2.png");
        assertEquals(element.advarsel, null);
        assertEquals(element.underelementer, null);
    }

    @Test
    public void klarer_ikke_aa_deserializere_element_med_tall() throws IOException {
        String json = "{\n" +
                "          \"sporsmal\": \"Sporsmal?\",\n" +
                "          \"svar\": 2,\n" +
                "          \"advarsel\": \"ADVARSEL!\",\n" +
                "          \"underelementer\": null\n" +
                "        }";

        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Element element = deserializer.deserialize(parser, ctxt);
        assertEquals(element, null);
    }
}