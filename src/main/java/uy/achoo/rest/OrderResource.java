package uy.achoo.rest;

import uy.achoo.controller.OrderController;
import uy.achoo.Wrappers.OrderAndOrderLinesWrapper;

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
public class OrderResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(OrderAndOrderLinesWrapper orderAndOrderLines) {
        Response response;
        try {
            OrderController.createOrder(orderAndOrderLines.getOrder(), orderAndOrderLines.getOrderLines());
            response = Response.status(200).entity(orderAndOrderLines).build();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            response = Response.status(500).entity(e).build();
        }
        return response;
    }

    @PUT
    @Path("rate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response rateOrder(@FormParam("orderId") Integer orderId, @FormParam("score") Integer score) {
        Response response;
        try {
            OrderController.rateOrder(orderId,score);
            response = Response.status(200).entity(score).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(e).build();
        }
        return response;
    }

    @GET
    @Path("{orderId}")
    public Response readOrder(@PathParam("orderId") Integer orderId) {
        Response response;
        try {
            OrderAndOrderLinesWrapper orderAndOrderLines = OrderController.readOrder(orderId);
            response = Response.status(200).entity(orderAndOrderLines).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(e).build();
        }
        return response;
    }

}
