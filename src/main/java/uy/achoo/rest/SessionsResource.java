package uy.achoo.rest;

import uy.achoo.Wrappers.JWTWrapper;
import uy.achoo.controller.UsersController;
import uy.achoo.model.tables.pojos.User;
import uy.achoo.util.JWTUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *
 * Sessions endpoint
 */
@Path("/sessions")
@Produces(MediaType.APPLICATION_JSON)
public class SessionsResource {


    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createSession(@FormParam("email") String email, @FormParam("password") String password) {
        Response response;
        try {
            User authenticatedUser = UsersController.fetchUserByEmailAndPassword(email, password);
            String jwt = JWTUtils.encodePayload(JWTUtils.introspect(authenticatedUser));
            response = Response.status(200).entity(new JWTWrapper(jwt)).build();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }
}
