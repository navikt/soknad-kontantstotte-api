package no.nav.kontantstotte.innsending.steg;


import no.nav.kontantstotte.innsending.VedleggMetadata;
import no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel;

import java.util.ArrayList;
import java.util.List;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class Barnehageplass {
    public String harBarnehageplass;
    public BarnehageplassVerdier barnBarnehageplassStatus;
    public String harBarnehageplassAntallTimer;
    public String harBarnehageplassDato;
    public String harBarnehageplassKommune;
    public String harSluttetIBarnehageKommune;
    public String harSluttetIBarnehageAntallTimer;
    public String harSluttetIBarnehageDato;
    public List<VedleggMetadata> harSluttetIBarnehageVedlegg;
    public String skalBegynneIBarnehageKommune;
    public String skalBegynneIBarnehageAntallTimer;
    public String skalBegynneIBarnehageDato;
    public String skalSlutteIBarnehageKommune;
    public String skalSlutteIBarnehageAntallTimer;
    public String skalSlutteIBarnehageDato;
    public List<VedleggMetadata> skalSlutteIBarnehageVedlegg;

    public Barnehageplass() {
        this.harSluttetIBarnehageVedlegg = new ArrayList<>();
        this.skalSlutteIBarnehageVedlegg = new ArrayList<>();
    }

    public enum BarnehageplassVerdier {
        garIkkeIBarnehage("garIkkeIBarnehage", GAR_IKKE_I_BARNEHAGE),
        harBarnehageplass("harBarnehageplass", GAR_I_BARNEHAGE),
        harSluttetIBarnehage("harSluttetIBarnehage", HAR_SLUTTET_I_BARNEHAGE),
        skalBegynneIBarnehage("skalBegynneIBarnehage", SKAL_BEGYNNE_I_BARNEHAGE),
        skalSlutteIBarnehage("skalSlutteIBarnehage", SKAL_SLUTTE_I_BARNEHAGE);

        String key;
        Tekstnokkel tekstNokkel;

        BarnehageplassVerdier(String key, Tekstnokkel tekstNokkel) {
            this.key = key;
            this.tekstNokkel = tekstNokkel;
        }

        public String getKey() {
            return key;
        }

        public Tekstnokkel getTekstNokkel() {
            return tekstNokkel;
        }
    }
}