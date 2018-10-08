package no.nav.kontantstotte.oppsummering.innsending.v2.mapping;


import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.bolk.Barn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.oppsummering.innsending.ArkivInnsendingService.hentFnrFraToken;

/**
 * Klassen benyttes til ny pdf generering.
 * mapBolker tar inn søknadsobjektet og mapper til ny
 * oppsummeringsobjekt som skal til ny html+pdf generator.
 *
 * For hver bolk man tar fra soknad til ny oppsummering skriver vi en map
 * funksjon som erstatter det gamle attributtet.
 */
public class SoknadTilOppsummering {
    public static final String BARN_TITTEL = "barn.tittel";
    public static final String BARN_UNDERTITTEL = "oppsummering.barn.subtittel";
    public static final String BARN_NAVN = "barn.navn";
    public static final String BARN_FODSELSDATO = "barn.fodselsdato";

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
        return barneBolk;
    }
}
