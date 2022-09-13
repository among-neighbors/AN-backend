package com.knud4.an.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 서버 HTTP request/response logging filter
 */
@Order(1)
@Component
public class LoggingFilter extends OncePerRequestFilter {
    protected static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MDC.put("traceId", UUID.randomUUID().toString().substring(30));
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(new RequestWrapper(request), new ResponseWrapper(response), filterChain);
        }
        MDC.clear();
    }

    protected void doFilterWrapped(RequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        String[] resource = request.getRequestURI().split("/");
        String lastResource = resource[resource.length - 1];
        boolean isLoggable = (!lastResource.equals("account-token") && !lastResource.equals("profile-token") && !lastResource.equals("an-ws"));

        try {
            if (isLoggable) {
                logRequest(request);
            }

            filterChain.doFilter(request, response);
        } finally {
            if (isLoggable) {
                logResponse(response);
            }

            response.copyBodyToResponse();
        }
    }

    private static void logRequest(RequestWrapper request) throws IOException {

        String queryString = request.getQueryString();
        log.info("Request : {} ip=[{}] uri=[{}] content-type=[{}]",
                request.getMethod(),
                request.getRemoteAddr(),
                queryString == null ? request.getRequestURI() : request.getRequestURI() + queryString,
                request.getContentType()
        );

        logPayload("Request", request.getContentType(), request.getInputStream());
    }

    private static void logResponse(ContentCachingResponseWrapper response) throws IOException {
        log.info("Response '{}'", response.getStatus());
        logPayload("Response", response.getContentType(), response.getContentInputStream());
    }

    private static void logPayload(String prefix, String contentType, InputStream inputStream) throws IOException {

        boolean visible = isVisible(MediaType.valueOf(contentType == null ? "application/json" : contentType));
        if (visible) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            if (content.length > 0) {
                String contentString = new String(content).replace("\n", "").replaceAll(" ", "");
                log.info("{} Payload: {}", prefix, contentString);
            }
        } else {
            log.info("{} Payload: Binary Content", prefix);
        }
    }

    private static boolean isVisible(MediaType mediaType) {

        final List<MediaType> VISIBLE_TYPES = Arrays.asList(
                MediaType.valueOf("text/*"),
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_XML,
                MediaType.valueOf("application/*+json"),
                MediaType.valueOf("application/*+xml"),
                MediaType.MULTIPART_FORM_DATA
        );

        return VISIBLE_TYPES.stream()
                .anyMatch(visibleType -> visibleType.includes(mediaType));
    }
}
