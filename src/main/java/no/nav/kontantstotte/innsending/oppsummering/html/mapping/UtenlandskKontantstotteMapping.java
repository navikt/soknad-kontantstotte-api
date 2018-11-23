package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.steg.UtenlandskKontantstotte;

import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class UtenlandskKontantstotteMapping extends BolkMapping {
    public UtenlandskKontantstotteMapping(Map<String, String> tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {

        UtenlandskKontantstotte utenlandskKontantstotte = soknad.getUtenlandskKontantstotte();

        Bolk bolk = new Bolk()
                .medTittel(tekst(UTENLANDSK_KONTANTSTOTTE_TITTEL));

        if(!utenlandskKontantstotte.mottarKontantstotteFraUtlandet){
            bolk.add(svar(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE, SVAR_NEI));
        }
        if(utenlandskKontantstotte.mottarKontantstotteFraUtlandet){
            bolk.add(svar(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE, SVAR_JA));
            bolk.add(svar(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE_TILLEGGSINFO,
                    utenlandskKontantstotte.mottarKontantstotteFraUtlandetTilleggsinfo));
        }
        return bolk;
    }


}
