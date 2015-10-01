package uy.achoo.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
public class AchooResource {

    @Inject
    public AchooResource() { }

    @GET
    @Path("hello")
    public String hello() {
        return "Hello, world!";
    }
}
