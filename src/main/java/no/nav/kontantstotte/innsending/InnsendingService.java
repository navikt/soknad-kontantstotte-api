package no.nav.kontantstotte.innsending;

import javax.ws.rs.core.Response;

public interface InnsendingService {

    Response sendInnSoknad(Soknad soknad); // TODO Burde ikke være avhengig av jax rs
}
