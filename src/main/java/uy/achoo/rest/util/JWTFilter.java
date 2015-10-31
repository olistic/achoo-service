package uy.achoo.rest.util;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import uy.achoo.controller.UsersController;
import uy.achoo.util.JWTUtils;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *
 * Filter to apply to resources based on the JWT received
 */
public class JWTFilter implements ContainerRequestFilter {

    // Exception thrown if user is unauthorized.
    private final static WebApplicationException UNAUTHORIZED =
            new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"realm\"")
                            .entity("Page requires login.").build());


    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) throws WebApplicationException{
        try {
            // Automatically allow certain requests.
            String method = containerRequest.getMethod();
            String path = containerRequest.getPath(true);
            if (method.equals("GET") && path.equals("users/authenticate"))
                return containerRequest;

            String token = JWTUtils.getTokenFromRequest(containerRequest);
            Map<String, Object> decoded = JWTUtils.decodeToken(token);
            if(UsersController.checkUsersPassword(decoded.get("email").toString(), decoded.get("password").toString(), true))
                return containerRequest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw UNAUTHORIZED;
    }
}