package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

public enum Tekstnokkel {
    SVAR_NEI("svar.nei"),
    SVAR_JA("svar.ja"),

    KRAV_TIL_SOKER_TITTEL("oppsummering.kravtilsoker.tittel"),
    KRAV_TIL_SOKER_BARN_IKKE_HJEMME("startside.krav.barnIkkeHjemme"),
    KRAV_TIL_SOKER_BODD_ELLER_JOBBET_I_NORGE_SISTE_FEM_AAR("startside.krav.boddEllerJobbetINorgeSisteFemAar"),
    KRAV_TIL_SOKER_BOR_SAMMEN_MED_BARNET("startside.krav.borSammenMedBarnet"),
    KRAV_TIL_SOKER_IKKE_AVTALT_DELT_BOSTED("startside.krav.ikkeAvtaltDeltBosted"),
    KRAV_TIL_SOKER_NORSK_STATSBORGER("startside.krav.norskStatsborger"),
    KRAV_TIL_SOKER_SKAL_BO_MED_BARNET_I_NORGE_NESTE_TOLV_MAANEDER("startside.krav.skalBoMedBarnetINorgeNesteTolvMaaneder"),

    BARN_TITTEL("barn.tittel"),
    BARN_UNDERTITTEL("oppsummering.barn.subtittel"),
    BARN_NAVN("barn.navn"),
    BARN_FODSELSDATO("barn.fodselsdato"),
    FAMILIEFORHOLD_TITTEL("familieforhold.tittel"),
    FAMILIEFORHOLD_BOR_SAMMEN("familieforhold.borForeldreneSammenMedBarnet.sporsmal"),
    FAMILIEFORHOLD_NAVN_ANNEN_FORELDER("oppsummering.familieforhold.annenForelderNavn.label"),
    FAMILIEFORHOLD_FNR_ANNEN_FORELDER("oppsummering.familieforhold.annenForelderFodselsnummer.label"),
    BARNEHAGEPLASS_TITTEL("barnehageplass.tittel"),
    HAR_BARNEHAGEPLASS("oppsummering.barnehageplass.harBarnehageplass"),
    BARN_BARNEHAGEPLASS_STATUS("barnehageplass.barnBarnehageplassStatus"),
    BARNEHAGEPLASS_HOYT_TIMEANTALL_ADVARSEL("advarsel.barnehageplass.timerIBarnehage"),
    GAR_IKKE_I_BARNEHAGE("barnehageplass.garIkkeIBarnehage"),
    GAR_I_BARNEHAGE("barnehageplass.harBarnehageplass"),
    HAR_SLUTTET_I_BARNEHAGE("barnehageplass.harSluttetIBarnehage"),
    SKAL_BEGYNNE_I_BARNEHAGE("barnehageplass.skalBegynneIBarnehage"),
    SKAL_SLUTTE_I_BARNEHAGE("barnehageplass.skalSlutteIBarnehage"),

    HAR_BARNEHAGEPLASS_DATO("barnehageplass.harBarnehageplass.dato.sporsmal"),
    HAR_BARNEHAGEPLASS_ANTALL_TIMER("barnehageplass.harBarnehageplass.antallTimer.sporsmal"),
    HAR_BARNEHAGEPLASS_KOMMUNE("barnehageplass.harBarnehageplass.kommune.sporsmal"),

    HAR_SLUTTET_I_BARNEHAGE_DATO("barnehageplass.harSluttetIBarnehage.dato.sporsmal"),
    HAR_SLUTTET_I_BARNEHAGE_ANTALL_TIMER("barnehageplass.harSluttetIBarnehage.antallTimer.sporsmal"),
    HAR_SLUTTET_I_BARNEHAGE_KOMMUNE("barnehageplass.harSluttetIBarnehage.kommune.sporsmal"),

    SKAL_BEGYNNE_I_BARNEHAGE_DATO("barnehageplass.skalBegynneIBarnehage.dato.sporsmal"),
    SKAL_BEGYNNE_I_BARNEHAGE_ANTALL_TIMER("barnehageplass.skalBegynneIBarnehage.antallTimer.sporsmal"),
    SKAL_BEGYNNE_I_BARNEHAGE_KOMMUNE("barnehageplass.skalBegynneIBarnehage.kommune.sporsmal"),

    SKAL_SLUTTE_I_BARNEHAGE_DATO("barnehageplass.skalSlutteIBarnehage.dato.sporsmal"),
    SKAL_SLUTTE_I_BARNEHAGE_ANTALL_TIMER("barnehageplass.skalSlutteIBarnehage.antallTimer.sporsmal"),
    SKAL_SLUTTE_I_BARNEHAGE_KOMMUNE("barnehageplass.skalSlutteIBarnehage.kommune.sporsmal"),

    UTENLANDSKE_YTELSER_TITTEL("utenlandskeYtelser.tittel"),
    UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND("oppsummering.utenlandskeYtelser.mottarYtelserFraUtland"),
    UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND("oppsummering.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland"),
    UTENLANDSKE_YTELSER_FORKLARING("oppsummering.utenlandskeYtelser.forklaring.label"),

    UTENLANDSK_KONTANTSTOTTE_TITTEL("utenlandskKontantstotte.tittel"),
    UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE("utenlandskKontantstotte.mottarKontantstotteFraUtlandet.sporsmal"),
    UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE_TILLEGGSINFO("utenlandskKontantstotte.mottarKontantstotteFraUtlandet.tilleggsinfo.sporsmal");

    private final String nokkel;

    Tekstnokkel(String nokkel) {
        this.nokkel = nokkel;
    }

    public String getNokkel() {
        return nokkel;
    }
}
