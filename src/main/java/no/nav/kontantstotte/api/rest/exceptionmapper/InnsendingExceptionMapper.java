package no.nav.kontantstotte.api.rest.exceptionmapper;

import no.nav.kontantstotte.innsending.InnsendingException;
import no.nav.kontantstotte.person.domain.FortroligAdresseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InnsendingExceptionMapper implements ExceptionMapper<InnsendingException> {

    private static final Logger logger = LoggerFactory.getLogger(InnsendingExceptionMapper.class);

    @Override
    public Response toResponse(InnsendingException e) {
        logger.warn(e.getMessage(), e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
