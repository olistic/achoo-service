package uy.achoo.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import uy.achoo.controller.OrdersController;
import uy.achoo.controller.UsersController;
import uy.achoo.customModel.CustomOrder;
import uy.achoo.model.tables.pojos.User;
import uy.achoo.rest.util.CORSResourceFilter;
import uy.achoo.util.EmailService;

import javax.inject.Inject;
import javax.mail.MessagingException;
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
 *         <p/>
 *         Users endpoint
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(CORSResourceFilter.class)
public class UsersResource {

    @Inject
    public UsersResource() {
    }

    @OPTIONS
    public Response corsCreate() {
        return Response.status(Response.Status.OK).build();
    }

    @GET
    public Response index() {
        Response response;
        try {
            List<User> users = UsersController.findAllUsers();
            response = Response.status(Response.Status.OK).entity(users).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(User user) {
        Response response;
        try {
            User createdUser = UsersController.createUser(user);
            if (createdUser != null) {
                EmailService.sendRegistrationMail(createdUser.getEmail(), createdUser.getFirstName());
            }
            response = Response.status(Response.Status.OK).entity(createdUser).build();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException |
                SQLException | MessagingException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

    @GET
    @Path("{userId}/orders")
    public Response listOrders(@PathParam("userId") Integer userId, @QueryParam("token") String token) {
        Response response;
        try {
            List<CustomOrder> orders = OrdersController.findAllOrdersOfUser(userId);
            response = Response.status(Response.Status.OK).entity(orders).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

    @POST
    @Path("reset-password")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response resetPassword(@FormParam("email") String email) {
        Response response;
        try {
            String newPassword = UsersController.resetPassword(email);
            if (newPassword != null) {
                EmailService.sendResetPasswordMail(email, newPassword);
            }
            response = Response.status(Response.Status.OK).entity(newPassword != null ? "\"" + newPassword + "\"" : null).build();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | SQLException | MessagingException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

    @GET
    @Path("available-email")
    public Response isEmailAvailable(@QueryParam("email") String email) {
        Response response;
        try {
            Boolean result = UsersController.isEmailAvailable(email);
            response = Response.status(Response.Status.OK).entity(result).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }
}
