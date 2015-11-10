package uy.achoo.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import uy.achoo.controller.UsersController;
import uy.achoo.model.tables.pojos.User;
import uy.achoo.rest.util.CORSResourceFilter;
import uy.achoo.util.JWTUtils;
import uy.achoo.wrappers.JWTWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *         <p/>
 *         Sessions endpoint
 */
@Path("/sessions")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(CORSResourceFilter.class)
public class SessionsResource {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createSession(@FormParam("email") String email, @FormParam("password") String password) {
        Response response;
        try {
            User authenticatedUser = UsersController.fetchUserByEmailAndPassword(email, password);
            if (authenticatedUser != null) {
                String jwt = JWTUtils.encodePayload(JWTUtils.introspect(authenticatedUser));
                response = Response.status(Response.Status.OK).entity(new JWTWrapper(jwt)).build();
            } else {
                response = Response.status(Response.Status.UNAUTHORIZED).entity(null).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }
}
