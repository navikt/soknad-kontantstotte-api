package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;


import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barn;
import no.nav.kontantstotte.oppsummering.bolk.Familieforhold;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SoknadTilOppsummering {


    public static final String SVAR_NEI = "svar.nei";
    public static final String BARN_TITTEL = "barn.tittel";
    public static final String BARN_UNDERTITTEL = "oppsummering.barn.subtittel";
    public static final String BARN_NAVN = "barn.navn";
    public static final String BARN_FODSELSDATO = "barn.fodselsdato";
    public static final String FAMILIEFORHOLD_TITTEL = "familieforhold.tittel";
    public static final String FAMILIEFORHOLD_BOR_SAMMEN = "familieforhold.borForeldreneSammenMedBarnet.sporsmal";

    public SoknadOppsummering map(Soknad soknad, Map<String, String> tekster) {

        return new SoknadOppsummering(soknad,
                mapBolker(soknad, tekster),
                tekster);
    }

    private List<Bolk> mapBolker(Soknad soknad, Map<String, String> tekster) {
        return Arrays.asList(
                nyBolk("personalia"),
                nyBolk("kravTilSoker"),
                mapBarn(soknad.mineBarn, tekster),
                nyBolk("barnehageplass"),
                nyBolk("familieforhold"),
                nyBolk("tilknytningTilUtland"),
                nyBolk("arbeidIUtlandet"),
                nyBolk("utenlandskeYtelser"),
                nyBolk("utenlandskKontantstotte"),
                nyBolk("oppsummering")
        );
    }

    private Bolk nyBolk(String bolknavn) {
        Bolk bolk = new Bolk();
        bolk.bolknavn = bolknavn;
        return bolk;
    }

    public Bolk mapBarn(Barn barn, Map<String, String> tekster) {
        Bolk barneBolk = new Bolk();
        barneBolk.tittel = tekster.get(BARN_TITTEL);
        barneBolk.undertittel = tekster.get(BARN_UNDERTITTEL);
        barneBolk.elementer = new ArrayList<>();
        barneBolk.elementer.add(Element.nyttSvar(tekster.get(BARN_NAVN), barn.navn));
        barneBolk.elementer.add(Element.nyttSvar(tekster.get(BARN_FODSELSDATO), barn.fodselsdato));
        return barneBolk;
    }


    public Bolk mapFamilieforhold(Familieforhold familieforhold, Map<String, String> tekster) {
        Bolk bolk = new Bolk();
        bolk.tittel = tekster.get(FAMILIEFORHOLD_TITTEL);
        bolk.elementer = new ArrayList<>();
        if("NEI".equalsIgnoreCase(familieforhold.borForeldreneSammenMedBarnet)){
            bolk.elementer.add(Element.nyttSvar(tekster.get(FAMILIEFORHOLD_BOR_SAMMEN), tekster.get(SVAR_NEI)));
        }
        return bolk;
    }



}
