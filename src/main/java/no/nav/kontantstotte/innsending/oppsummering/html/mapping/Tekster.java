package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import java.util.Map;

public class Tekster {

    Map<String,String> tekster;

    public Tekster(Map<String,String> tekster) {
        this.tekster = tekster;
    }

    public String hentTekst(String nokkel) {
        return tekster.get(nokkel);
    }

    public String hentTekst(String nokkel, boolean flertallsForm) {
        return (flertallsForm && tekster.get(nokkel+".flertall") != null)
                ? tekster.get(nokkel+".flertall")
                : tekster.get(nokkel);
    }
}
