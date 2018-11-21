package no.nav.kontantstotte.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
            garIkkeIBarnehage,
            harBarnehageplass,
            harSluttetIBarnehage,
            skalBegynneIBarnehage,
            skalSlutteIBarnehage
        }
    }

    public static class Familieforhold {
        public String borForeldreneSammenMedBarnet;
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
        public String mottarYtelserFraUtland;
        public String mottarYtelserFraUtlandForklaring;
        public String mottarAnnenForelderYtelserFraUtland;
        public String mottarAnnenForelderYtelserFraUtlandForklaring;
    }

    public static class UtenlandskKontantstotte {
        public String mottarKontantstotteFraUtlandet;
        public String mottarKontantstotteFraUtlandetTilleggsinfo;
    }

    public static class SokerKrav {
        public String barnIkkeHjemme;
        public String boddEllerJobbetINorgeSisteFemAar;
        public String borSammenMedBarnet;
        public String ikkeAvtaltDeltBosted;
        public String norskStatsborger;
        public String skalBoMedBarnetINorgeNesteTolvMaaneder;
    }

    public static class Person {
        public String fnr;

        public Person() {
        }

        public Person(String fnr) {
            this.fnr = fnr;
        }
    }

    public static class Oppsummering {
        public String bekreftelse;
    }

    public static class ArbeidIUtlandet {
        public String arbeiderIUtlandetEllerKontinentalsokkel;
        public String arbeiderIUtlandetEllerKontinentalsokkelForklaring;
        public String arbeiderAnnenForelderIUtlandet;
        public String arbeiderAnnenForelderIUtlandetForklaring;
    }
}
