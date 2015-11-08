package uy.achoo.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import uy.achoo.controller.OrdersController;
import uy.achoo.rest.util.AuthenticatedResourceFilter;
import uy.achoo.rest.util.CORSResourceFilter;
import uy.achoo.util.JWTUtils;
import uy.achoo.wrappers.OrderAndOrderLinesWrapper;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(CORSResourceFilter.class)
public class OrdersResource {
    @GET
    @ResourceFilters(AuthenticatedResourceFilter.class)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listOrdersOfUser(@Context HttpHeaders headers) {
        Response response;
        try {
            Map<String, Object> authorizationPayload  = JWTUtils.decodeToken(JWTUtils.getTokenFromHeaders(headers));
            List<OrderAndOrderLinesWrapper> orders = OrdersController.findAllOrdersOfUser((Integer) authorizationPayload.get("id"));
            response = Response.status(200).entity(orders).build();
        } catch (SQLException | NullPointerException | ServletException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

    @POST
    @ResourceFilters(AuthenticatedResourceFilter.class)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(OrderAndOrderLinesWrapper orderAndOrderLines, @Context HttpHeaders headers) {
        Response response;
        try {
            Map<String, Object> authorizationPayload  = JWTUtils.decodeToken(JWTUtils.getTokenFromHeaders(headers));
            orderAndOrderLines.getOrder().setUserId((Integer) authorizationPayload.get("id"));
            orderAndOrderLines.getOrder().setDate(new Timestamp(new Date().getTime()));
            OrdersController.createOrder(orderAndOrderLines.getOrder(), orderAndOrderLines.getOrderLines());
            response = Response.status(200).entity(orderAndOrderLines).build();
        } catch (SQLException | NullPointerException | MessagingException | ServletException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

    @PUT
    @ResourceFilters(AuthenticatedResourceFilter.class)
    @Path("{orderId}/score")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response rate(@PathParam("orderId") Integer orderId, @FormParam("score") Integer score) {
        Response response;
        try {
            if (score <= 5 && score >= 0)
                OrdersController.rateOrder(orderId, score);
            response = Response.status(200).entity(score).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

    @GET
    @ResourceFilters(AuthenticatedResourceFilter.class)
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
