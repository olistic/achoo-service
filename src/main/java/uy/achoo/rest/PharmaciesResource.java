package uy.achoo.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import uy.achoo.controller.OrdersController;
import uy.achoo.controller.PharmaciesController;
import uy.achoo.controller.ProductsController;
import uy.achoo.customModel.CustomPharmacy;
import uy.achoo.model.tables.pojos.Pharmacy;
import uy.achoo.model.tables.pojos.Product;
import uy.achoo.rest.util.AuthenticatedResourceFilter;
import uy.achoo.rest.util.CORSResourceFilter;
import uy.achoo.wrappers.OrderAndOrderLinesWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
@Path("/pharmacies")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(CORSResourceFilter.class)
public class PharmaciesResource {

    @OPTIONS
    public Response corsCreate() {
        return Response.status(Response.Status.OK).build();
    }

    @GET
    public Response listPharmacies(@QueryParam("query") String query,
                                   @QueryParam("latitude") Double latitude,
                                   @QueryParam("longitude") Double longitude) {
        Response response;
        try {
            List<CustomPharmacy> pharmacies;
            if (query == null) {
                 pharmacies = PharmaciesController.findAllPharmacies();
            } else {
                pharmacies = PharmaciesController.searchPharmaciesByNameOrProductName(query,
                        latitude, longitude);
            }
            response = Response.status(Response.Status.OK).entity(pharmacies).build();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }


    @GET
    @Path("{pharmacyId}")
    public Response readPharmacy(@PathParam("pharmacyId") Integer pharmacyId) {
        CustomPharmacy pharmacy = PharmaciesController.readPharmacy(pharmacyId);
        return Response.status(Response.Status.OK).entity(pharmacy).build();
    }

    @GET
    @ResourceFilters(AuthenticatedResourceFilter.class)
    @Path("{pharmacyId}/orders")
    public Response listOrders(@PathParam("pharmacyId") Integer pharmacyId) {
        Response response;
        try {
            List<OrderAndOrderLinesWrapper> orderAndOrderLines = OrdersController.findAllOrdersOfDrugStore(pharmacyId);
            response = Response.status(Response.Status.OK).entity(orderAndOrderLines).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

    @GET
    @Path("{pharmacyId}/products")
    public Response listProducts(@PathParam("pharmacyId") Integer pharmacyId) {
        Response response;
        try {
            List<Product> products = ProductsController.searchAllProductsOfDrugstor(pharmacyId);
            response = Response.status(Response.Status.OK).entity(products).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

}
