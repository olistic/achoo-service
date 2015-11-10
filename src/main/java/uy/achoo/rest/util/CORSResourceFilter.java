package uy.achoo.rest.util;

import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 * <p/>
 * Allow Cross Origin Requests
 */

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *         <p/>
 *         Filter for resources that require authentication
 */
public class CORSResourceFilter implements ResourceFilter {

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return null;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return (containerRequest, containerResponse) -> {
            containerResponse.getHttpHeaders().add("Access-Control-Allow-Origin", "*");
            containerResponse.getHttpHeaders().add("Access-Control-Allow-Headers",
                    "Origin, Content-Type, Accept, Authorization");
            containerResponse.getHttpHeaders().add("Access-Control-Allow-Credentials", "true");
            containerResponse.getHttpHeaders().add("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");

            return containerResponse;
        };
    }
}
