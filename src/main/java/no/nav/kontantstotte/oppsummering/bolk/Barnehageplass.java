package no.nav.kontantstotte.oppsummering.bolk;

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
        garIkkeIBarnehage("garIkkeIBarnehage", "barnehageplass.garIkkeIBarnehage"),
        harBarnehageplass("harBarnehageplass", "barnehageplass.harBarnehageplass"),
        harSluttetIBarnehage("harSluttetIBarnehage", "barnehageplass.harSluttetIBarnehage"),
        skalBegynneIBarnehage("skalBegynneIBarnehage", "barnehageplass.skalBegynneIBarnehage"),
        skalSlutteIBarnehage("skalSlutteIBarnehage", "barnehageplass.skalSlutteIBarnehage"),
        Ubesvart("Ubesvart", "");

        String key;
        String tekstNokkel;

        BarnehageplassVerdier(String key, String tekstNokkel) {
            this.key = key;
            this.tekstNokkel = tekstNokkel;
        }

        public String getKey() {
            return key;
        }

        public String getTekstNokkel() {
            return tekstNokkel;
        }
    }
}