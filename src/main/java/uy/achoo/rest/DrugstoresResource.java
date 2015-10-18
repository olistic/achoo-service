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
    public Response index() {
        Response response;
        try {
            List<Drugstore> drugstores = DrugstoresController.findAllDrugstores();
            response = Response.status(200).entity(drugstores).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

    @GET
    @Path("search")
    public Response searchDrugstoresByProduct(@QueryParam("name") String productNamePart,
                                              @QueryParam("latitude") Double latitude,
                                              @QueryParam("longitude") Double longitude) {
        Response response;
        try {
            List<DrugstoreJPA> drugstores = DrugstoresController.searchDrugstoresByProductName(productNamePart,
                    latitude, longitude);
            response = Response.status(200).entity(drugstores).build();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }
    /**
    @GET
    @Path("search/product_id")
    public Response searchDrugstoresByProduct(@QueryParam("id") Integer productId) {
        Response response;
        try {
            List<Drugstore> drugstores = DrugstoresController.searchDrugstoresByProductId(productId);
            response = Response.status(200).entity(drugstores).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }**/

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
