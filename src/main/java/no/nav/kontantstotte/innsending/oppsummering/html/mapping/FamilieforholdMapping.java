package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.steg.Familieforhold;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;

import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class FamilieforholdMapping extends BolkMapping {
    public FamilieforholdMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        Bolk bolk = new Bolk()
                .medTittel(tekst(FAMILIEFORHOLD_TITTEL));

        Familieforhold familieforhold = soknad.getFamilieforhold();
        if (!familieforhold.borForeldreneSammenMedBarnet) {
            return bolk.add(svar(FAMILIEFORHOLD_BOR_SAMMEN, SVAR_NEI));
        }

        return bolk.add(svar(FAMILIEFORHOLD_BOR_SAMMEN, SVAR_JA))
                .add(svar(FAMILIEFORHOLD_NAVN_ANNEN_FORELDER, familieforhold.annenForelderNavn))
                .add(svar(FAMILIEFORHOLD_FNR_ANNEN_FORELDER, familieforhold.annenForelderFodselsnummer));
    }
}
