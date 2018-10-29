package no.nav.kontantstotte.api.rest.exceptionmapper;

import no.nav.kontantstotte.person.domain.SikkerhetsbegrensningException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class SikkerhetsbegrensningExceptionMapper implements ExceptionMapper<SikkerhetsbegrensningException> {

    private static final Logger logger = LoggerFactory.getLogger(SikkerhetsbegrensningExceptionMapper.class);

    @Override
    public Response toResponse(SikkerhetsbegrensningException e) {
        logger.info(e.getMessage(), e);
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
