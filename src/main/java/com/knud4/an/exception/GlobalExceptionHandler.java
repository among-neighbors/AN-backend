package com.knud4.an.exception;

import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal
            (Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorResult<String> error = ApiUtil.error(status.value(), ex.getMessage());

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    @ExceptionHandler({
            IllegalStateException.class,
            NotFoundException.class,
            NotAuthenticatedException.class})
    protected ResponseEntity<?> handleIllegalStateException(Exception e) {
        ApiErrorResult<String> error = ApiUtil.error(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(error);
    }
}
