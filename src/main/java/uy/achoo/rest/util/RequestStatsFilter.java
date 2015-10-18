package uy.achoo.rest.util;

import com.twitter.common.stats.RequestStats;

import javax.servlet.*;
import java.io.IOException;

/**
 * A servlet filter that uses Twitter Commons request stats to keep tabs on requests
 *
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public final class RequestStatsFilter implements Filter {

    final RequestStats requestStats;

    public RequestStatsFilter(String statName) {
        this.requestStats = new RequestStats(statName);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Long start = System.nanoTime();
        chain.doFilter(request, response);

        // Takes micros
        requestStats.requestComplete((System.nanoTime() - start) / 1000);
    }

    @Override
    public void destroy() {
    }
}
