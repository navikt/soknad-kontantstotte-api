package no.nav.kontantstotte.innsending;

import no.nav.kontantstotte.api.rest.dto.InnsendingsResponsDto;

public interface InnsendingService {

    InnsendingsResponsDto sendInnSoknad(Soknad soknad); // TODO Burde ikke være avhengig av jax rs
}
