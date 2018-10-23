package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.UtenlandskKontantstotte;

import java.util.ArrayList;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class UtenlandskKontantstotteMapping extends BolkMapping {
    public UtenlandskKontantstotteMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        UtenlandskKontantstotte utenlandskKontantstotte = soknad.utenlandskKontantstotte;
        Bolk bolk = new Bolk();
        bolk.tittel = tekster.get(UTENLANDSK_KONTANTSTOTTE_TITTEL.getNokkel());
        bolk.elementer = new ArrayList<>();
        if("NEI".equalsIgnoreCase(utenlandskKontantstotte.mottarKontantstotteFraUtlandet)){
            bolk.elementer.add(Element.nyttSvar(tekster.get(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE.getNokkel()),
                    tekster.get(SVAR_NEI.getNokkel())));
        }if("JA".equalsIgnoreCase(utenlandskKontantstotte.mottarKontantstotteFraUtlandet)){
            bolk.elementer.add(Element.nyttSvar(tekster.get(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE.getNokkel()),
                    tekster.get(SVAR_JA.getNokkel())));
            bolk.elementer.add(Element.nyttSvar(tekster.get(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE_TILLEGGSINFO.getNokkel()),
                    utenlandskKontantstotte.mottarKontantstotteFraUtlandetTilleggsinfo));
        }
        return bolk;
    }


}
