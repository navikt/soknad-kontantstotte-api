package no.nav.kontantstotte.api.rest.exceptionmapper;

import no.nav.kontantstotte.innsyn.domain.InnsynOppslagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InnsynOppslagExceptionMapper implements ExceptionMapper<InnsynOppslagException> {

    private static final Logger logger = LoggerFactory.getLogger(InnsynOppslagExceptionMapper.class);

    @Override
    public Response toResponse(InnsynOppslagException e) {
        logger.error(e.getMessage(), e);
        return Response.serverError().build();
    }
}
