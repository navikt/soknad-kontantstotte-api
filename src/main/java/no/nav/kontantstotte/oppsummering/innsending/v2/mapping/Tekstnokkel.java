package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;

public enum Tekstnokkel {
    SVAR_NEI("svar.nei"),
    SVAR_JA("svar.ja"),
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
    SKAL_SLUTTE_I_BARNEHAGE_KOMMUNE("barnehageplass.skalSlutteIBarnehage.kommune.sporsmal");

    private final String nokkel;

    Tekstnokkel(String nokkel) {
        this.nokkel = nokkel;
    }

    public String getNokkel() {
        return nokkel;
    }
}
