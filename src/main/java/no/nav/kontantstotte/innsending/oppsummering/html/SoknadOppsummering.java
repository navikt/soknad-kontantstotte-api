package no.nav.kontantstotte.innsending.oppsummering.html;


import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.steg.Person;

import java.util.List;
import java.util.Map;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.BEKREFTELSE;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.INNSENDING_LABEL;
import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.TITTEL;

class SoknadOppsummering {
    private Soknad soknad;
    private MetaData metaData;
    private List<Bolk> bolker;
    private Map<String, String> tekster;

    public SoknadOppsummering() {

    }

    public SoknadOppsummering(Soknad soknad, String fnr, String innsendingsTidspunkt, List<Bolk> bolker, Map<String, String> tekster) {
        MetaDataElement metaDataInnsendingsTidspunkt = new MetaDataElement(tekster.get(INNSENDING_LABEL.getNokkel()), innsendingsTidspunkt);
        Person person = new Person(fnr);
        MetaDataElement metaDataBekreftelse = new MetaDataElement(tekster.get(BEKREFTELSE.getNokkel()), null);

        this.metaData = new MetaData(tekster.get(TITTEL.getNokkel()), metaDataInnsendingsTidspunkt, person, metaDataBekreftelse);
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
