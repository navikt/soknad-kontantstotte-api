package no.nav.kontantstotte.api.rest.exceptionmapper;

import no.nav.kontantstotte.person.domain.FortroligAdresseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class FortroligAdresseExceptionMapper implements ExceptionMapper<FortroligAdresseException> {

    private static final Logger logger = LoggerFactory.getLogger(FortroligAdresseExceptionMapper.class);

    @Override
    public Response toResponse(FortroligAdresseException e) {
        logger.info(e.getMessage(), e);
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
