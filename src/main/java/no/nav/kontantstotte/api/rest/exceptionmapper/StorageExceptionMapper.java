package no.nav.kontantstotte.api.rest.exceptionmapper;

import no.nav.kontantstotte.storage.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class StorageExceptionMapper implements ExceptionMapper<StorageException> {

    private static final Logger log = LoggerFactory.getLogger(StorageExceptionMapper.class);

    @Override
    public Response toResponse(StorageException e) {
        log.error(e.getMessage(), e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
