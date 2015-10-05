package uy.achoo.rest.util;

import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import uy.achoo.controller.UsersController;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by alfredo on 04/10/15.
 */
public class AuthFilter implements ContainerRequestFilter {

    // Exception thrown if user is unauthorized.
    private final static WebApplicationException UNAUTTHORIZED =
            new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"realm\"")
                            .entity("Page requires login.").build());

    @Override
    public ContainerRequest filter(ContainerRequest containerRequest)
            throws WebApplicationException {

        // Automatically allow certain requests.
        String method = containerRequest.getMethod();
        String path = containerRequest.getPath(true);
        if (method.equals("GET") && path.equals("users/authenticate"))
            return containerRequest;

        // Get the authentication passed in HTTP headers parameters
        String auth = containerRequest.getHeaderValue("authorization");
        if (auth == null)
            throw UNAUTTHORIZED;

        auth = auth.replaceFirst("[Bb]asic ", "");
        String userColonPass = Base64.base64Decode(auth);
        String[] chunks = userColonPass.split(":");
        String email = chunks[0];
        String password = chunks[1];

        try {
            if (UsersController.checkUsersPassword(email, password))
                return containerRequest;
        } catch (UnsupportedEncodingException|NoSuchAlgorithmException|SQLException e) {
            e.printStackTrace();
        }
        throw UNAUTTHORIZED;
    }
}
