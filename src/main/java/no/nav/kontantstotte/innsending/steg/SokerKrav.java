package no.nav.kontantstotte.innsending.steg;

public class SokerKrav {
    public String barnIkkeHjemme;
    public String boddEllerJobbetINorgeSisteFemAar;
    public String borSammenMedBarnet;
    public String ikkeAvtaltDeltBosted;
    public String norskStatsborger;
    public String skalBoMedBarnetINorgeNesteTolvMaaneder;

    public SokerKrav(String barnIkkeHjemme,
                     String boddEllerJobbetINorgeSisteFemAar,
                     String borSammenMedBarnet,
                     String ikkeAvtaltDeltBosted,
                     String norskStatsborger,
                     String skalBoMedBarnetINorgeNesteTolvMaaneder) {
        this.barnIkkeHjemme = barnIkkeHjemme;
        this.boddEllerJobbetINorgeSisteFemAar = boddEllerJobbetINorgeSisteFemAar;
        this.borSammenMedBarnet = borSammenMedBarnet;
        this.ikkeAvtaltDeltBosted = ikkeAvtaltDeltBosted;
        this.norskStatsborger = norskStatsborger;
        this.skalBoMedBarnetINorgeNesteTolvMaaneder = skalBoMedBarnetINorgeNesteTolvMaaneder;
    }
}
