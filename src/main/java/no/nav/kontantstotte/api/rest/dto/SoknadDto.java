package no.nav.kontantstotte.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SoknadDto {

    public Person person = new Person();
    public SokerKrav kravTilSoker = new SokerKrav();
    public Familieforhold familieforhold = new Familieforhold();
    public Barnehageplass barnehageplass = new Barnehageplass();
    public ArbeidIUtlandet arbeidIUtlandet = new ArbeidIUtlandet();
    public UtenlandskKontantstotte utenlandskKontantstotte = new UtenlandskKontantstotte();
    public Barn mineBarn = new Barn();
    public TilknytningTilUtland tilknytningTilUtland = new TilknytningTilUtland();
    public UtenlandskeYtelser utenlandskeYtelser = new UtenlandskeYtelser();
    public Oppsummering oppsummering = new Oppsummering();
    public String sprak;

    public static class AnnenForelder {
        @JsonProperty("annenForelderNavn")
        public String navn;
        @JsonProperty("annenForelderPersonnummer")
        public String personnummer;
        @JsonProperty("annenForelderYrkesaktivINorgeEOSIMinstFemAar")
        public String yrkesaktivINorgeEOSIMinstFemAar;

    }

    public static class Barn {
        public String navn;
        public String fodselsdato;

    }

    public static class Barnehageplass {
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean harBarnehageplass;
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
            garIkkeIBarnehage,
            harBarnehageplass,
            harSluttetIBarnehage,
            skalBegynneIBarnehage,
            skalSlutteIBarnehage
        }
    }

    public static class Familieforhold {
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean borForeldreneSammenMedBarnet;
        public String annenForelderNavn;
        public String annenForelderFodselsnummer;
    }

    public static class TilknytningTilUtland {
        public String annenForelderBoddEllerJobbetINorgeMinstFemAar;
        public String annenForelderBoddEllerJobbetINorgeMinstFemAarForklaring;
        public String boddEllerJobbetINorgeMinstFemAar;
        public String boddEllerJobbetINorgeMinstFemAarForklaring;
    }

    public static class UtenlandskeYtelser {
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean mottarYtelserFraUtland;
        public String mottarYtelserFraUtlandForklaring;
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean mottarAnnenForelderYtelserFraUtland;
        public String mottarAnnenForelderYtelserFraUtlandForklaring;
    }

    public static class UtenlandskKontantstotte {
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean mottarKontantstotteFraUtlandet;
        public String mottarKontantstotteFraUtlandetTilleggsinfo;
    }

    public static class SokerKrav {
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean barnIkkeHjemme;
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean boddEllerJobbetINorgeSisteFemAar;
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean borSammenMedBarnet;
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean ikkeAvtaltDeltBosted;
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean norskStatsborger;
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean skalBoMedBarnetINorgeNesteTolvMaaneder;
    }

    public static class Person {
        public String fnr;
    }

    public static class Oppsummering {
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean bekreftelse;
    }

    public static class ArbeidIUtlandet {
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean arbeiderIUtlandetEllerKontinentalsokkel;
        public String arbeiderIUtlandetEllerKontinentalsokkelForklaring;
        @JsonDeserialize(using = JaNeiBooleanDeserializer.class)
        public boolean arbeiderAnnenForelderIUtlandet;
        public String arbeiderAnnenForelderIUtlandetForklaring;
    }
}
