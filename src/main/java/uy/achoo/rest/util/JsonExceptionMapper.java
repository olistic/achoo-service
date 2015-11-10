package uy.achoo.rest.util;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *         <p/>
 *         Mapper to display JSON map exceptions
 */
@Provider
public class JsonExceptionMapper implements ExceptionMapper<Exception> {

    private Logger logger;

    @Inject
    public JsonExceptionMapper(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Response toResponse(final Exception e) {
        String message = e.getMessage();
        String type = e.getClass().getSimpleName();

        Map<String, String> entity = Maps.newHashMap();
        entity.put("type", type);
        entity.put("message", message);

        logger.log(Level.WARNING, type, e);

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(entity)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
