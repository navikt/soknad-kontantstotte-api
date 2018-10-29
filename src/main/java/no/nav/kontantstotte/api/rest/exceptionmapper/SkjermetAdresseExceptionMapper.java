package no.nav.kontantstotte.api.rest.exceptionmapper;

import no.nav.kontantstotte.person.domain.SkjermetAdresseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class SkjermetAdresseExceptionMapper implements ExceptionMapper<SkjermetAdresseException> {

    private static final Logger logger = LoggerFactory.getLogger(SkjermetAdresseExceptionMapper.class);

    @Override
    public Response toResponse(SkjermetAdresseException e) {
        logger.warn(e.getMessage(), e);
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
