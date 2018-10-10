package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;


import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barn;
import no.nav.kontantstotte.oppsummering.bolk.Familieforhold;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.oppsummering.innsending.v2.mapping.Tekstnokkel.*;


/**
 * Klassen benyttes til ny pdf generering.
 * mapBolker tar inn søknadsobjektet og mapper til ny
 * oppsummeringsobjekt som skal til ny html+pdf generator.
 *
 * For hver bolk man tar fra soknad til ny oppsummering skriver vi en map
 * funksjon som erstatter det gamle attributtet.
 */
public class SoknadTilOppsummering {

    public SoknadOppsummering map(Soknad soknad, Map<String, String> tekster, String fnr) {
        return new SoknadOppsummering(soknad,
                fnr,
                mapBolker(soknad, tekster),
                tekster);
    }

    private List<Bolk> mapBolker(Soknad soknad, Map<String, String> tekster) {
        return Arrays.asList(
                nyBolk("kravTilSoker"),
                mapBarn(soknad.mineBarn, tekster),
                nyBolk("barnehageplass"),
                mapFamilieforhold(soknad.familieforhold, tekster),
                nyBolk("tilknytningTilUtland"),
                nyBolk("arbeidIUtlandet"),
                nyBolk("utenlandskeYtelser"),
                nyBolk("utenlandskKontantstotte"),
                nyBolk("oppsummering")
        );
    }

    private Bolk nyBolk(String bolknavn) {
        Bolk Bolk = new Bolk();
        Bolk.bolknavn = bolknavn;
        return Bolk;
    }


    public Bolk mapBarn(Barn barn, Map<String, String> tekster) {
        Bolk bolk = new Bolk();
        bolk.tittel = tekster.get(BARN_TITTEL.getNokkel());
        bolk.undertittel = tekster.get(BARN_UNDERTITTEL.getNokkel());
        bolk.elementer = new ArrayList<>();
        bolk.elementer.add(Element.nyttSvar(tekster.get(BARN_NAVN.getNokkel()), barn.navn));
        bolk.elementer.add(Element.nyttSvar(tekster.get(BARN_FODSELSDATO.getNokkel()), barn.fodselsdato));
        return bolk;
    }


    public Bolk mapFamilieforhold(Familieforhold familieforhold, Map<String, String> tekster) {
        Bolk bolk = new Bolk();
        bolk.tittel = tekster.get(FAMILIEFORHOLD_TITTEL.getNokkel());
        bolk.elementer = new ArrayList<>();
        if("NEI".equalsIgnoreCase(familieforhold.borForeldreneSammenMedBarnet)){
            bolk.elementer.add(Element.nyttSvar(tekster.get(FAMILIEFORHOLD_BOR_SAMMEN.getNokkel()), tekster.get(SVAR_NEI.getNokkel())));
        }if("JA".equalsIgnoreCase(familieforhold.borForeldreneSammenMedBarnet)){
            bolk.elementer.add(Element.nyttSvar(tekster.get(FAMILIEFORHOLD_BOR_SAMMEN.getNokkel()), tekster.get(SVAR_JA.getNokkel())));
            bolk.elementer.add(Element.nyttSvar(tekster.get(FAMILIEFORHOLD_NAVN_ANNEN_FORELDER.getNokkel()), familieforhold.annenForelderNavn));
            bolk.elementer.add(Element.nyttSvar(tekster.get(FAMILIEFORHOLD_FNR_ANNEN_FORELDER.getNokkel()), familieforhold.annenForelderFodselsnummer));
        }
        return bolk;
    }



}
