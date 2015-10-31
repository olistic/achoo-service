package uy.achoo.rest.util;

import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *
 * Filter for resources that require authentication
 */
public class AuthenticationRequiredFilter implements ResourceFilter {
    @Override
    public ContainerRequestFilter getRequestFilter() {
        return new JWTFilter();
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }
}

