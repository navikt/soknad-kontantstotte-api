package no.nav.kontantstotte.oppsummering.bolk;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Barnehageplass {
    public String harBarnehageplass;
    public BarnehageplassVerdier barnBarnehageplassStatus;
    public String harBarnehageplassAntallTimer;
    public String harBarnehageplassDato;
    public String harBarnehageplassKommune;
    public String harSluttetIBarnehageKommune;
    public String harSluttetIBarnehageAntallTimer;
    public String harSluttetIBarnehageDato;
    public String skalBegynneIBarnehageKommune;
    public String skalBegynneIBarnehageAntallTimer;
    public String skalBegynneIBarnehageDato;
    public String skalSlutteIBarnehageKommune;
    public String skalSlutteIBarnehageAntallTimer;
    public String skalSlutteIBarnehageDato;

    public enum BarnehageplassVerdier {
        garIkkeIBarnehage("garIkkeIBarnehage", "barnehageplass.garIkkeIBarnehage", Collections.emptyList()),
        harBarnehageplass("harBarnehageplass", "barnehageplass.harBarnehageplass",
                Arrays.asList(
                        "barnehageplass.harBarnehageplass.dato.sporsmal",
                        "barnehageplass.harBarnehageplass.antallTimer.sporsmal",
                        "barnehageplass.harBarnehageplass.kommune.sporsmal"
                )),
        harSluttetIBarnehage("harSluttetIBarnehage", "barnehageplass.harSluttetIBarnehage",
                Arrays.asList(
                        "barnehageplass.harSluttetIBarnehage.dato.sporsmal",
                        "barnehageplass.harSluttetIBarnehage.antallTimer.sporsmal",
                        "barnehageplass.harSluttetIBarnehage.kommune.sporsmal"
                )),
        skalBegynneIBarnehage("skalBegynneIBarnehage", "barnehageplass.skalBegynneIBarnehage",
                Arrays.asList(
                        "barnehageplass.skalBegynneIBarnehage.dato.sporsmal",
                        "barnehageplass.skalBegynneIBarnehage.antallTimer.sporsmal",
                        "barnehageplass.skalBegynneIBarnehage.kommune.sporsmal"
                )),
        skalSlutteIBarnehage("skalSlutteIBarnehage", "barnehageplass.skalSlutteIBarnehage",
                Arrays.asList(
                        "barnehageplass.skalSlutteIBarnehage.dato.sporsmal",
                        "barnehageplass.skalSlutteIBarnehage.antallTimer.sporsmal",
                        "barnehageplass.skalSlutteIBarnehage.kommune.sporsmal"
                )),
        Ubesvart("Ubesvart", "", Collections.emptyList());

        String key;
        String keyTekstNokkel;
        List<String> sporsmalTekstNokler;

        BarnehageplassVerdier(String key, String keyTekstNokkel, List<String> sporsmalTekstNokler) {
            this.key = key;
            this.keyTekstNokkel = keyTekstNokkel;
            this.sporsmalTekstNokler = sporsmalTekstNokler;
        }

        public String getKey() {
            return key;
        }

        public String getKeyTekstNokkel() {
            return keyTekstNokkel;
        }

        public List<String> getSporsmalTekstNokler() {
            return sporsmalTekstNokler;
        }
    }
}