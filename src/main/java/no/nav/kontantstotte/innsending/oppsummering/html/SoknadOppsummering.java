package no.nav.kontantstotte.innsending.oppsummering.html;


import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.steg.Person;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

class SoknadOppsummering {
    private Soknad soknad;
    private MetaData metaData;
    private List<Bolk> bolker;
    private Map<String, String> tekster;
    private static String SKJEMANUMMER = "NAV 34-00.08";

    public SoknadOppsummering() {

    }

    public SoknadOppsummering(Soknad soknad, String fnr, String innsendingsTidspunkt, List<Bolk> bolker, Map<String, String> tekster) {
        MetaDataElement metaDataInnsendingsTidspunkt = new MetaDataElement(tekster.get(INNSENDING_LABEL.getNokkel()), innsendingsTidspunkt);
        Person person = new Person(fnr);
        MetaDataElement metaDataBekreftelse = new MetaDataElement(tekster.get(BEKREFTELSE.getNokkel()), null);

        MetaDataElement metaDataFastsattDato = new MetaDataElement(tekster.get(FASTSATT_LABEL.getNokkel()), tekster.get(FASTSATTDATO.getNokkel()));
        MetaDataElement metaDataEndretDato = new MetaDataElement(tekster.get(ENDRET_LABEL.getNokkel()), tekster.get(ENDRET_DATO.getNokkel()));

        this.metaData = new MetaData.Builder()
                .tittel(tekster.get(TITTEL.getNokkel()))
                .innsendingsTidspunkt(metaDataInnsendingsTidspunkt)
                .person(person)
                .bekreftelse(metaDataBekreftelse)
                .skjemanummer(SKJEMANUMMER)
                .endretDato(metaDataEndretDato)
                .fastsattdato(metaDataFastsattDato)
                .build();

        this.soknad = soknad;
        this.bolker = bolker;
        this.tekster = tekster;
    }

    public Soknad getSoknad() {
        return soknad;
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
