package no.nav.kontantstotte.api.filter;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class CORSResponseFilter implements Filter {

    private static final List<String> ALLOWED_ORIGINS = asList(
            "https://soknad-kontantstotte-t.nav.no",
            "https://soknad-kontantstotte-q.nav.no",
            "https://soknad-kontantstotte-dev-sbs.dev.nav.no",
            "https://soknad-kontantstotte.nav.no",
            "https://soknad-kontantstotte.dev.nav.no",
            "https://soknad-kontantstotte-gcp.intern.nav.no"
            );

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String origin = request.getHeader("Origin");
        if (ALLOWED_ORIGINS.contains(origin)) {
            response.addHeader("Access-Control-Allow-Origin", origin);
            response.addHeader("Access-Control-Allow-Headers",
                    "origin, content-type, accept, authorization");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
