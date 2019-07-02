package no.nav.kontantstotte.innsending;

public interface InnsendingService {

    Soknad sendInnSoknad(Soknad soknad);

    void sendStrukturertSoknad(Soknad soknad);
}
