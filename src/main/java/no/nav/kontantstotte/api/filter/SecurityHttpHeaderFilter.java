package no.nav.kontantstotte.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class SecurityHttpHeaderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = ((HttpServletResponse) response);

        httpServletResponse.addHeader("Cache-Control", "no-store");

        httpServletResponse.addHeader("X-Frame-Options", "DENY");
        httpServletResponse.addHeader("X-Content-Type-Options", "nosniff");
        httpServletResponse.addHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        httpServletResponse.addHeader("X-XSS-Protection", "1; mode=block");
        httpServletResponse.addHeader("X-Permitted-Cross-Domain-Policies", "none");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
