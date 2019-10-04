package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.familie.ks.kontrakter.søknad.AktørArbeidYtelseUtland;
import no.nav.familie.ks.kontrakter.søknad.Standpunkt;
import no.nav.familie.ks.kontrakter.søknad.Søknad;
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

    public Bolk mapNy(Søknad søknad) {
        Bolk kontantstøtteIUtlandBolk = new Bolk();

        kontantstøtteIUtlandBolk.tittel = tekster.hentTekst(UTENLANDSK_KONTANTSTOTTE_TITTEL.getNokkel());
        kontantstøtteIUtlandBolk.elementer = new ArrayList<>();

        AktørArbeidYtelseUtland søkerKontantstøtteUtland = MappingUtils.hentArbeidYtelseUtlandForSøker(søknad);

        if (Standpunkt.NEI.equals(søkerKontantstøtteUtland.getKontantstøtteIUtlandet())) {
            kontantstøtteIUtlandBolk.elementer.add(
                    Element.nyttSvar(
                            tekster.hentTekst(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE.getNokkel()), tekster.hentTekst(SVAR_NEI.getNokkel())
                    ));
        }

        if (Standpunkt.JA.equals(søkerKontantstøtteUtland.getKontantstøtteIUtlandet())) {
            kontantstøtteIUtlandBolk.elementer.add(
                    Element.nyttSvar(
                            tekster.hentTekst(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE.getNokkel()), tekster.hentTekst(SVAR_JA.getNokkel())
                    ));
            kontantstøtteIUtlandBolk.elementer.add(
                    Element.nyttSvar(
                            tekster.hentTekst(UTENLANDSK_KONTANTSTOTTE_MOTTAR_STOTTE_TILLEGGSINFO.getNokkel()), søkerKontantstøtteUtland.getKontantstøtteIUtlandetForklaring()
                    ));
        }

        return kontantstøtteIUtlandBolk;
    }


}
