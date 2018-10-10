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
    FAMILIEFORHOLD_FNR_ANNEN_FORELDER("oppsummering.familieforhold.annenForelderFodselsnummer.label");

    private final String nokkel;

    Tekstnokkel(String nokkel) {
        this.nokkel = nokkel;
    }

    public String getNokkel() {
        return nokkel;
    }
}
