package uy.achoo.rest;


import uy.achoo.controller.ProductsController;
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
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductsResource {

    @GET
    public Response listAllProducts() {
        Response response;
        try {
            List<Product> products = ProductsController.listAllProducts();
            response = Response.status(200).entity(products).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

    @GET
    @Path("{productId}")
    public Response read(@PathParam("productId") Integer productId) {
        Response response;
        try {
            Product product = ProductsController.readProduct(productId);
            response = Response.status(200).entity(product).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }

    @GET
    @Path("search/name")
    public Response searchByName(@QueryParam("name") String productNamePart) {
        Response response;
        try {
            List<Product> products = ProductsController.searchProductsByName(productNamePart);
            response = Response.status(200).entity(products).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(500).entity(null).build();
        }
        return response;
    }
}
