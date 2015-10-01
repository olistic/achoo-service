package uy.achoo.rest;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import uy.achoo.rest.util.JsonExceptionMapper;
import uy.achoo.rest.util.RequestStatsFilter;

import java.util.HashSet;
import java.util.Set;

/**
 * Configures our REST service
 *
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class RestModule extends ServletModule {

    @Override
    protected void configureServlets() {
        super.configureServlets();

        // JSON mapper, maps JSON to/from POJOs
        bind(JacksonJsonProvider.class).in(Singleton.class);
        // Turns exceptions into JSON responses
        bind(JsonExceptionMapper.class).in(Singleton.class);

        // Serve all URLs through Guice
        serve("/*").with(GuiceContainer.class);

        // The actual REST Endpoints
        bind(AchooResource.class).in(Singleton.class);

        // Stats
        filter("/v1/hello").through(new RequestStatsFilter("v1_hello"));
    }
}
