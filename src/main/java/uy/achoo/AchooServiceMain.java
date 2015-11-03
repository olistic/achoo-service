package uy.achoo;

import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;
import com.twitter.common.application.AbstractApplication;
import com.twitter.common.application.Lifecycle;
import com.twitter.common.application.modules.HttpModule;
import com.twitter.common.application.modules.LogModule;
import com.twitter.common.application.modules.StatsModule;
import com.twitter.common.args.Arg;
import com.twitter.common.args.CmdLine;
import com.twitter.common.net.http.GuiceServletConfig;
import com.twitter.common.net.http.HttpServerDispatch;
import org.mortbay.jetty.servlet.Context;
import uy.achoo.rest.RestModule;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public final class AchooServiceMain extends AbstractApplication {

    @CmdLine(name = "base_arg", help = "Base argument")
    public static final Arg<Boolean> BASE_ARG = Arg.create(true);

    @Inject
    private Logger logger;

    @Inject
    private Lifecycle lifecycle;

    @Inject
    private HttpServerDispatch httpServer;

    @Inject
    private GuiceServletConfig servletConfig;

    @Override
    public void run() {
        logger.info("Service started");

        addRestSupport();

        lifecycle.awaitShutdown();
    }

    @Override
    public Iterable<? extends Module> getModules() {
        return Arrays.asList(
                new HttpModule(),
                new LogModule(),
                new RestModule(),
                new StatsModule()
        );
    }

    private void addRestSupport() {
        Context context = httpServer.getRootContext();
        context.addFilter(GuiceFilter.class, "/*", 0);
        context.addEventListener(servletConfig);
    }
}
