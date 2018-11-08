package no.nav.kontantstotte.api.rest.exceptionmapper;

import no.nav.kontantstotte.person.domain.PersonOppslagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class PersonOppslagExceptionMapper implements ExceptionMapper<PersonOppslagException> {

    private static final Logger logger = LoggerFactory.getLogger(PersonOppslagExceptionMapper.class);

    @Override
    public Response toResponse(PersonOppslagException e) {
        logger.warn(e.getMessage(), e);
        return Response.serverError().build();
    }
}
