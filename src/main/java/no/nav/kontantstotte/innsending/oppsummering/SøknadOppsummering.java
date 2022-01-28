package no.nav.kontantstotte.innsending.oppsummering;

import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.MetaData;
import no.nav.kontantstotte.innsending.oppsummering.html.MetaDataElement;
import no.nav.kontantstotte.innsending.steg.Person;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.TITTEL;

public class SøknadOppsummering {

    private Søknad søknad;
    private MetaData metaData;
    private List<Bolk> bolker;
    private Map<String, String> tekster;

    public SøknadOppsummering(Søknad søknad, Person person, String innsendingsTidspunkt, List<Bolk> bolker, Map<String, String> tekster) {
        MetaDataElement metaDataInnsendingsTidspunkt = new MetaDataElement(tekster.get(INNSENDING_LABEL.getNokkel()), innsendingsTidspunkt);
        String samletBekreftelse = tekster.get(BEKREFTELSE_OPPLYSNINGER.getNokkel()) + "<br><br>" + tekster.get(BEKREFTELSE_PLIKTER.getNokkel());
        MetaDataElement metaDataBekreftelse = new MetaDataElement(samletBekreftelse, null);

        MetaDataElement metaDataFastsattDato = new MetaDataElement(tekster.get(FASTSATT_LABEL.getNokkel()), tekster.get(FASTSATTDATO.getNokkel()));
        MetaDataElement metaDataEndretDato = new MetaDataElement(tekster.get(ENDRET_LABEL.getNokkel()), tekster.get(ENDRET_DATO.getNokkel()));

        String SKJEMANUMMER = "NAV 34-00.08";
        this.metaData = new MetaData.Builder()
                .tittel(tekster.get(TITTEL.getNokkel()))
                .innsendingsTidspunkt(metaDataInnsendingsTidspunkt)
                .person(person)
                .bekreftelse(metaDataBekreftelse)
                .skjemanummer(SKJEMANUMMER)
                .endretDato(metaDataEndretDato)
                .fastsattdato(metaDataFastsattDato)
                .build();

        this.søknad = søknad;
        this.bolker = bolker;
        this.tekster = tekster;
    }

    public Søknad getSøknad() {
        return søknad;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public List<Bolk> getBolker() {
        return bolker;
    }

    public Map<String, String> getTekster() {
        return tekster;
    }
}
