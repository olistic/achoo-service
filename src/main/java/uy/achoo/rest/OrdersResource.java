package uy.achoo.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import uy.achoo.controller.OrdersController;
import uy.achoo.customModel.CustomOrder;
import uy.achoo.rest.util.CORSResourceFilter;
import uy.achoo.util.JWTUtils;

import javax.mail.MessagingException;
import javax.ws.rs.*;
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

    @OPTIONS
    public Response corsCreate() {
        return Response.status(Response.Status.OK).build();
    }

    @GET
    public Response listOrdersOfUser(@QueryParam("token") String token) {
        Response response;
        try {
            Map<String, Object> authorizationPayload = JWTUtils.decodeToken(token);
            List<CustomOrder> orders = OrdersController.findAllOrdersOfUser((Integer) authorizationPayload.get("id"));
            response = Response.status(Response.Status.OK).entity(orders).build();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

    @POST
    public Response create(CustomOrder order, @QueryParam("token") String token) {
        Response response;
        try {
            Map<String, Object> authorizationPayload = JWTUtils.decodeToken(token);
            order.setUserId((Integer) authorizationPayload.get("id"));
            order.setDate(new Timestamp(new Date().getTime()));
            CustomOrder insertedOrder = OrdersController.createOrder(order);
            response = Response.status(Response.Status.OK).entity(insertedOrder).build();
        } catch (SQLException | NullPointerException | MessagingException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

    @POST
    @Path("{orderId}/score")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response rate(@PathParam("orderId") Integer orderId, @FormParam("score") Integer score, @QueryParam("token") String token) {
        Response response;
        Map<String, Object> authorizationPayload = JWTUtils.decodeToken(token);
        try {
            if (authorizationPayload != null && score <= 5 && score >= 0)
                OrdersController.rateOrder(orderId, score, (Integer) authorizationPayload.get("id"));
            response = Response.status(Response.Status.OK).entity(score).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

    @GET
    @Path("{orderId}")
    public Response read(@PathParam("orderId") Integer orderId, @QueryParam("token") String token) {
        Response response;
        try {
            CustomOrder order = OrdersController.readOrder(orderId);
            Map<String, Object> authorizationPayload = JWTUtils.decodeToken(token);
            if (authorizationPayload != null && authorizationPayload.get("id").equals(order.getUserId())) {
                response = Response.status(Response.Status.OK).entity(order).build();
            } else {
                response = Response.status(Response.Status.UNAUTHORIZED).entity(null).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

}
