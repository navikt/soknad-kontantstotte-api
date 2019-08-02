package no.nav.kontantstotte.api.filter;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import no.nav.log.MDCConstants;

@Component
@Order(3)
public class CallIdFilter implements Filter {

    private static final Random RANDOM = new Random();

    public static String generateCallId() {
        int randomNr = RANDOM.nextInt(2147483647);
        long systemTime = System.currentTimeMillis();
        StringBuilder callId = (new StringBuilder("CallId_")).append(systemTime).append('_').append(randomNr);
        return callId.toString();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String callid = request.getHeader("Nav-Call-Id");
        MDC.put(MDCConstants.MDC_CALL_ID, Objects.requireNonNullElseGet(callid, CallIdFilter::generateCallId));

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
