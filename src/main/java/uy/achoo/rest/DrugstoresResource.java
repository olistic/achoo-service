package uy.achoo.rest;

import uy.achoo.Wrappers.OrderAndOrderLinesWrapper;
import uy.achoo.controller.DrugstoresController;
import uy.achoo.controller.OrdersController;
import uy.achoo.controller.ProductsController;
import uy.achoo.customModel.DrugstoreJPA;
import uy.achoo.model.tables.pojos.Drugstore;
import uy.achoo.model.tables.pojos.Product;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
@Path("/drugstores")
@Produces(MediaType.APPLICATION_JSON)
public class DrugstoresResource {

    @GET
    public Response listDrugstores(@QueryParam("name") String productNamePart,
                                   @QueryParam("latitude") Double latitude,
                                   @QueryParam("longitude") Double longitude) {
        Response response;
        try {
            if(productNamePart == null){
                List<Drugstore> drugstores = DrugstoresController.findAllDrugstores();
                response = Response.status(200).entity(drugstores).build();
            }else{
                List<DrugstoreJPA> drugstores = DrugstoresController.searchDrugstoresByNameOrProductName(productNamePart,
                        latitude, longitude);
                response = Response.status(200).entity(drugstores).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }


    @GET
    @Path("{drugstoreId}")
    public Response readDrugstore(@PathParam("drugstoreId") Integer drugstoreId) {
        Drugstore drugstore = DrugstoresController.readDrugstore(drugstoreId);
        Response response = Response.status(200).entity(drugstore).build();
        return response;
    }

    @GET
    @Path("{drugstoreId}/orders")
    public Response listOrders(@PathParam("drugstoreId") Integer drugstoreId) {
        Response response;
        try {
            List<OrderAndOrderLinesWrapper> orderAndOrderLines = OrdersController.findAllOrdersOfDrugStore(drugstoreId);
            response = Response.status(200).entity(orderAndOrderLines).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

    @GET
    @Path("{drugstoreId}/products")
    public Response listProducts(@PathParam("drugstoreId") Integer drugstoreId) {
        Response response;
        try {
            List<Product> products = ProductsController.searchAllProductsOfDrugstor(drugstoreId);
            response = Response.status(200).entity(products).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

}
