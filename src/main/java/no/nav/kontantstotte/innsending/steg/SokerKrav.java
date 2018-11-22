package no.nav.kontantstotte.innsending.steg;

public class SokerKrav {
    public boolean barnIkkeHjemme;
    public boolean boddEllerJobbetINorgeSisteFemAar;
    public boolean borSammenMedBarnet;
    public boolean ikkeAvtaltDeltBosted;
    public boolean norskStatsborger;
    public boolean skalBoMedBarnetINorgeNesteTolvMaaneder;

    public SokerKrav(boolean barnIkkeHjemme,
                     boolean boddEllerJobbetINorgeSisteFemAar,
                     boolean borSammenMedBarnet,
                     boolean ikkeAvtaltDeltBosted,
                     boolean norskStatsborger,
                     boolean skalBoMedBarnetINorgeNesteTolvMaaneder) {
        this.barnIkkeHjemme = barnIkkeHjemme;
        this.boddEllerJobbetINorgeSisteFemAar = boddEllerJobbetINorgeSisteFemAar;
        this.borSammenMedBarnet = borSammenMedBarnet;
        this.ikkeAvtaltDeltBosted = ikkeAvtaltDeltBosted;
        this.norskStatsborger = norskStatsborger;
        this.skalBoMedBarnetINorgeNesteTolvMaaneder = skalBoMedBarnetINorgeNesteTolvMaaneder;
    }
}
