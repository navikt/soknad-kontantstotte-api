package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

public enum Tekstnokkel {
    TITTEL("kontantstotte.tittel"),
    BEKREFTELSE_PLIKTER("veiledningsside.bekreftelse.oppsummering"),
    BEKREFTELSE_OPPLYSNINGER("oppsummering.bekreftelse.label"),
    INNSENDING_LABEL("oppsummering.innsendingsdato"),
    ENDRET_DATO("kontantstotte.endretDato"),
    ENDRET_LABEL("kontantstotte.endretDato.label"),
    FASTSATT_LABEL("kontantstotte.fastsattdato.label"),
    FASTSATTDATO("kontantstotte.fastsattdato"),
    SVAR_NEI("svar.nei"),
    SVAR_JA("svar.ja"),
    SVAR_JA_I_NORGE("tilknytningTilUtland.svar.jaINorge"),
    SVAR_JA_I_EOS("tilknytningTilUtland.svar.jaIEOS"),
    SOKER_SVAR_JA_LEGGER_SAMMEN_PERIODER_EOS("tilknytningTilUtland.svar.soker.jaLeggerSammenPerioderEOS"),
    ANNEN_FORELDER_SVAR_JA_LEGGER_SAMMEN_PERIODER_EOS("tilknytningTilUtland.svar.annenForelder.jaLeggerSammenPerioderEOS"),

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
    BARN_ADVARSEL("advarsel.flerebarn.utenlink"),
    FAMILIEFORHOLD_TITTEL("familieforhold.tittel"),
    FAMILIEFORHOLD_BOR_SAMMEN("familieforhold.borForeldreneSammenMedBarnet.sporsmal"),
    FAMILIEFORHOLD_NAVN_ANNEN_FORELDER("oppsummering.familieforhold.annenForelderNavn.label"),
    FAMILIEFORHOLD_FNR_ANNEN_FORELDER("oppsummering.familieforhold.annenForelderFodselsnummer.label"),
    BARNEHAGEPLASS_TITTEL("barnehageplass.tittel"),
    HAR_BARNEHAGEPLASS("barnehageplass.harPlass"),
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
    HAR_SLUTTET_I_BARNEHAGE_VEDLEGG("barnehageplass.harSluttetIBarnehage.vedlegg.sporsmal"),

    SKAL_BEGYNNE_I_BARNEHAGE_DATO("barnehageplass.skalBegynneIBarnehage.dato.sporsmal"),
    SKAL_BEGYNNE_I_BARNEHAGE_ANTALL_TIMER("barnehageplass.skalBegynneIBarnehage.antallTimer.sporsmal"),
    SKAL_BEGYNNE_I_BARNEHAGE_KOMMUNE("barnehageplass.skalBegynneIBarnehage.kommune.sporsmal"),

    SKAL_SLUTTE_I_BARNEHAGE_DATO("barnehageplass.skalSlutteIBarnehage.dato.sporsmal"),
    SKAL_SLUTTE_I_BARNEHAGE_ANTALL_TIMER("barnehageplass.skalSlutteIBarnehage.antallTimer.sporsmal"),
    SKAL_SLUTTE_I_BARNEHAGE_KOMMUNE("barnehageplass.skalSlutteIBarnehage.kommune.sporsmal"),
    SKAL_SLUTTE_I_BARNEHAGE_VEDLEGG("barnehageplass.skalSlutteIBarnehage.vedlegg.sporsmal"),

    ARBEID_I_UTLANDET_TITTEL("arbeidIUtlandet.tittel"),
    ARBEID_I_UTLANDET_ARBEIDER_ANNEN_FORELDER_I_UTLANDET("arbeidIUtlandet.arbeiderAnnenForelderIUtlandet.sporsmal"),
    ARBEID_I_UTLANDET_ELLER_KONTINENTALSOKKEL("arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkel.sporsmal"),
    ARBEID_I_UTLANDET_FORKLARING("arbeidIUtlandet.forklaring.hjelpetekst"),

    UTENLANDSKE_YTELSER_TITTEL("utenlandskeYtelser.tittel"),
    UTENLANDSKE_YTELSER_MOTTAR_YTELSER_FRA_UTLAND("utenlandskeYtelser.mottarYtelserFraUtland.sporsmal"),
    UTENLANDSKE_YTELSER_MOTTAR_ANNEN_FORELDER_YTELSER_FRA_UTLAND("utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland.sporsmal"),
    UTENLANDSKE_YTELSER_FORKLARING("utenlandskeYtelser.forklaring.hjelpetekst"),

    UTENLANDSK_KONTANTSTOTTE_TITTEL("utenlandskKontantstotte.tittel"),
    UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE("utenlandskKontantstotte.mottarKontantstotteFraUtlandet.sporsmal"),
    UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE_TILLEGGSINFO("utenlandskKontantstotte.mottarKontantstotteFraUtlandet.tilleggsinfo.sporsmal"),

    TILKNYTNING_TIL_UTLAND_TITTEL("tilknytningTilUtland.tittel"),
    TILKNYTNING_TIL_UTLAND_SOKER_IKKE_BODD_I_NORGE("tilknytningTilUtland.svar.soker.nei"),
    TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE("tilknytningTilUtland.svar.annenForelder.nei"),
    TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR("tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar.sporsmal"),
    TILKNYTNING_TIL_UTLAND_BODD_I_NORGE_MINST_FEM_AAR_ANNEN_FORELDER("tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar.sporsmal"),
    TILKNYTNING_TIL_UTLAND_SOKER_IKKE_BODD_I_NORGE_ADVARSEL("tilknytningTilUtland.advarsel.nei.soker"),
    TILKNYTNING_TIL_UTLAND_ANNEN_FORELDER_IKKE_BODD_I_NORGE_ADVARSEL("tilknytningTilUtland.advarsel.nei.annenForelder"),
    TILKNYTNING_TIL_UTLAND_FORKLARING("tilknytningTilUtland.forklaring.hjelpetekst");



    private final String nokkel;

    Tekstnokkel(String nokkel) {
        this.nokkel = nokkel;
    }

    public String getNokkel() {
        return nokkel;
    }
}
