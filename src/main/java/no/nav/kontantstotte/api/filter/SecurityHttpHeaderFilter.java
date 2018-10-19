package no.nav.kontantstotte.api.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityHttpHeaderFilter implements Filter  {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = ((HttpServletResponse) response);

        httpServletResponse.addHeader("Cache-Control", "no-cache, no-store");
        httpServletResponse.addHeader("Expires", "0");
        httpServletResponse.addHeader("Pragma", "no-cache");

        httpServletResponse.addHeader("X-Frame-Options", "SAMEORIGIN");
        httpServletResponse.addHeader("X-Content-Type-Options", "nosniff");
        httpServletResponse.addHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        httpServletResponse.addHeader("X-XSS-Protection", "1; mode=block");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
