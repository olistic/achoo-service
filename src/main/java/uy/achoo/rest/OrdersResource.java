package uy.achoo.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import uy.achoo.controller.OrdersController;
import uy.achoo.Wrappers.OrderAndOrderLinesWrapper;
import uy.achoo.rest.util.AuthenticationRequiredFilter;

import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrdersResource {
    @POST
    @ResourceFilters(AuthenticationRequiredFilter.class)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(OrderAndOrderLinesWrapper orderAndOrderLines) {
        Response response;
        try {
            OrdersController.createOrder(orderAndOrderLines.getOrder(), orderAndOrderLines.getOrderLines());
            response = Response.status(200).entity(orderAndOrderLines).build();
        } catch (SQLException | NullPointerException |MessagingException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

    @PUT
    @ResourceFilters(AuthenticationRequiredFilter.class)
    @Path("rate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response rate(@FormParam("orderId") Integer orderId, @FormParam("score") Integer score) {
        Response response;
        try {
            OrdersController.rateOrder(orderId, score);
            response = Response.status(200).entity(score).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

    @GET
    @ResourceFilters(AuthenticationRequiredFilter.class)
    @Path("{orderId}")
    public Response read(@PathParam("orderId") Integer orderId) {
        Response response;
        try {
            OrderAndOrderLinesWrapper orderAndOrderLines = OrdersController.readOrder(orderId);
            response = Response.status(200).entity(orderAndOrderLines).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

}
