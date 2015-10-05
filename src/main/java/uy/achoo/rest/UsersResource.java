package uy.achoo.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import uy.achoo.Wrappers.OrderAndOrderLinesWrapper;
import uy.achoo.controller.OrdersController;
import uy.achoo.controller.UsersController;
import uy.achoo.model.tables.pojos.User;
import uy.achoo.rest.util.AuthenticationRequiredFilter;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Inject
    public UsersResource() {
    }

    @GET
    public Response index() {
        Response response;
        try {
            List<User> users = UsersController.findAllUsers();
            response = Response.status(200).entity(users).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(e).build();
        }
        return response;
    }

    @POST
    @ResourceFilters(AuthenticationRequiredFilter.class)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(User user) {
        Response response;
        try {
            User createdUser = UsersController.createUser(user);
            response = Response.status(200).entity(createdUser).build();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(e).build();
        }
        return response;
    }

    @GET
    @ResourceFilters(AuthenticationRequiredFilter.class)
    @Path("{userId}/orders")
    public Response listOrders(@PathParam("userId") Integer userId) {
        Response response;
        try {
            List<OrderAndOrderLinesWrapper> orderAndOrderLines = OrdersController.findAllOrdersOfUser(userId);
            response = Response.status(200).entity(orderAndOrderLines).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(e).build();
        }
        return response;
    }

    @POST
    @Path("authenticate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticate(@FormParam("email") String email, @FormParam("password") String password) {
        Response response;
        try {
            boolean authenticated = UsersController.checkUsersPassword(email, password);
            response = Response.status(200).entity(authenticated).build();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(e).build();
        }
        return response;
    }
}
