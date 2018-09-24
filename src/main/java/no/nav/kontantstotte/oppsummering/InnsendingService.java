package no.nav.kontantstotte.oppsummering;

import javax.ws.rs.core.Response;

public interface InnsendingService {

    Response sendInnSoknad(Soknad soknad); // TODO Burde ikke være avhengig av jax rs
}
