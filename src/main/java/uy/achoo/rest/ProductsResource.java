package uy.achoo.rest;


import com.sun.jersey.spi.container.ResourceFilters;
import uy.achoo.controller.ProductsController;
import uy.achoo.model.tables.pojos.Product;
import uy.achoo.rest.util.CORSResourceFilter;

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
 *         <p/>
 *         Products endpoint
 */
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(CORSResourceFilter.class)
public class ProductsResource {

    @OPTIONS
    public Response corsCreate() {
        return Response.status(Response.Status.OK).build();
    }

    @GET
    public Response listAllProducts() {
        Response response;
        try {
            List<Product> products = ProductsController.listAllProducts();
            response = Response.status(Response.Status.OK).entity(products).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

    @GET
    @Path("{productId}")
    public Response read(@PathParam("productId") Integer productId) {
        Response response;
        try {
            Product product = ProductsController.readProduct(productId);
            response = Response.status(Response.Status.OK).entity(product).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }

    @GET
    @Path("search/name")
    public Response searchByName(@QueryParam("name") String productNamePart) {
        Response response;
        try {
            List<Product> products = ProductsController.searchProductsByName(productNamePart);
            response = Response.status(Response.Status.OK).entity(products).build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
        }
        return response;
    }
}
