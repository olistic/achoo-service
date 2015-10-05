package uy.achoo.rest.util;

import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * Created by alfredo on 04/10/15.
 */
public class AuthenticationRequiredFilter implements ResourceFilter {
    @Override
    public ContainerRequestFilter getRequestFilter() {
        return new AuthFilter();
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }
}

