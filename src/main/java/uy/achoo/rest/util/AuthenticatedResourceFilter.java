package uy.achoo.rest.util;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import uy.achoo.controller.UsersController;
import uy.achoo.util.JWTUtils;

import javax.servlet.ServletException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *
 * Filter for resources that require authentication
 */
public class AuthenticatedResourceFilter implements ResourceFilter {

    private final static WebApplicationException UNAUTHORIZED =
            new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"realm\"")
                            .entity("Page requires login.").build());

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return containerRequest -> {
            try {
                String token = JWTUtils.getTokenFromRequest(containerRequest);
                Map<String, Object> decodedToken = JWTUtils.decodeToken(token);
                if (UsersController.checkUserPassword(
                        decodedToken.get("email").toString(),
                        decodedToken.get("password").toString(), true)) {
                    return containerRequest;
                }
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            throw UNAUTHORIZED;
        };
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }
}

