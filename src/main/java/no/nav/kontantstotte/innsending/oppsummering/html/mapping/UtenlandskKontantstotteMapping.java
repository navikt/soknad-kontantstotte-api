package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.UtenlandskKontantstotte;

import java.util.ArrayList;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class UtenlandskKontantstotteMapping extends BolkMapping {
    public UtenlandskKontantstotteMapping(Tekster tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        UtenlandskKontantstotte utenlandskKontantstotte = soknad.utenlandskKontantstotte;
        Bolk bolk = new Bolk();
        bolk.tittel = tekster.hentTekst(UTENLANDSK_KONTANTSTOTTE_TITTEL.getNokkel());
        bolk.elementer = new ArrayList<>();
        if ("NEI".equalsIgnoreCase(utenlandskKontantstotte.mottarKontantstotteFraUtlandet)) {
            bolk.elementer.add(
                    Element.nyttSvar(
                            tekster.hentTekst(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE.getNokkel()), tekster.hentTekst(SVAR_NEI.getNokkel())
                    ));
        }
        if ("JA".equalsIgnoreCase(utenlandskKontantstotte.mottarKontantstotteFraUtlandet)) {
            bolk.elementer.add(
                    Element.nyttSvar(
                            tekster.hentTekst(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE.getNokkel()), tekster.hentTekst(SVAR_JA.getNokkel())
                    ));
            bolk.elementer.add(
                    Element.nyttSvar(
                            tekster.hentTekst(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE_TILLEGGSINFO.getNokkel()), utenlandskKontantstotte.mottarKontantstotteFraUtlandetTilleggsinfo
                    ));
        }
        return bolk;
    }


}
