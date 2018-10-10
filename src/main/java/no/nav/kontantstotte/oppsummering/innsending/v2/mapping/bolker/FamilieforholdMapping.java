package no.nav.kontantstotte.oppsummering.innsending.v2.mapping.bolker;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Familieforhold;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Bolk;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Element;

import java.util.ArrayList;
import java.util.Map;

import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering.SVAR_JA;
import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadTilOppsummering.SVAR_NEI;

public class FamilieforholdMapping implements BolkMapping {
    public static final String FAMILIEFORHOLD_TITTEL = "familieforhold.tittel";
    public static final String FAMILIEFORHOLD_BOR_SAMMEN = "familieforhold.borForeldreneSammenMedBarnet.sporsmal";
    public static final String FAMILIEFORHOLD_NAVN_ANNEN_FORELDER = "oppsummering.familieforhold.annenForelderNavn.label";
    public static final String FAMILIEFORHOLD_FNR_ANNEN_FORELDER = "oppsummering.familieforhold.annenForelderFodselsnummer.label";

    @Override
    public Bolk map(Soknad soknad, Map<String, String> tekster, Unleash unleash) {
        Bolk bolk = new Bolk();
        Familieforhold familieforhold = soknad.familieforhold;
        bolk.tittel = tekster.get(FAMILIEFORHOLD_TITTEL);
        bolk.elementer = new ArrayList<>();

        if("NEI".equalsIgnoreCase(familieforhold.borForeldreneSammenMedBarnet)){
            bolk.elementer.add(Element.nyttSvar(tekster.get(FAMILIEFORHOLD_BOR_SAMMEN), tekster.get(SVAR_NEI)));
        }if("JA".equalsIgnoreCase(familieforhold.borForeldreneSammenMedBarnet)){
            bolk.elementer.add(Element.nyttSvar(tekster.get(FAMILIEFORHOLD_BOR_SAMMEN), tekster.get(SVAR_JA)));
            bolk.elementer.add(Element.nyttSvar(tekster.get(FAMILIEFORHOLD_NAVN_ANNEN_FORELDER), familieforhold.annenForelderNavn));
            bolk.elementer.add(Element.nyttSvar(tekster.get(FAMILIEFORHOLD_FNR_ANNEN_FORELDER), familieforhold.annenForelderFodselsnummer));
        }
        return bolk;
    }
}
