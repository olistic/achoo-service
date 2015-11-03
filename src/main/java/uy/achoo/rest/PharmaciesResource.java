package uy.achoo.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import uy.achoo.rest.util.CORSResourceFilter;
import uy.achoo.wrappers.OrderAndOrderLinesWrapper;
import uy.achoo.controller.PharmaciesController;
import uy.achoo.controller.OrdersController;
import uy.achoo.controller.ProductsController;
import uy.achoo.customModel.CustomPharmacy;
import uy.achoo.model.tables.pojos.Pharmacy;
import uy.achoo.model.tables.pojos.Product;
import uy.achoo.rest.util.AuthenticatedResourceFilter;

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

    @GET
    public Response listPharmacies(@QueryParam("name") String productNamePart,
                                   @QueryParam("latitude") Double latitude,
                                   @QueryParam("longitude") Double longitude) {
        Response response;
        try {
            if(productNamePart == null){
                List<Pharmacy> pharmacies = PharmaciesController.findAllPharmacies();
                response = Response.status(200).entity(pharmacies).build();
            }else{
                List<CustomPharmacy> pharmacies = PharmaciesController.searchPharmaciesByNameOrProductName(productNamePart,
                        latitude, longitude);
                response = Response.status(200).entity(pharmacies).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }


    @GET
    @Path("{pharmacyId}")
    public Response readPharmacy(@PathParam("pharmacyId") Integer pharmacyId) {
        CustomPharmacy pharmacy = PharmaciesController.readPharmacy(pharmacyId);
        return Response.status(200).entity(pharmacy).build();
    }

    @GET
    @ResourceFilters(AuthenticatedResourceFilter.class)
    @Path("{pharmacyId}/orders")
    public Response listOrders(@PathParam("pharmacyId") Integer pharmacyId) {
        Response response;
        try {
            List<OrderAndOrderLinesWrapper> orderAndOrderLines = OrdersController.findAllOrdersOfDrugStore(pharmacyId);
            response = Response.status(200).entity(orderAndOrderLines).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

    @GET
    @Path("{pharmacyId}/products")
    public Response listProducts(@PathParam("pharmacyId") Integer pharmacyId) {
        Response response;
        try {
            List<Product> products = ProductsController.searchAllProductsOfDrugstor(pharmacyId);
            response = Response.status(200).entity(products).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

}
