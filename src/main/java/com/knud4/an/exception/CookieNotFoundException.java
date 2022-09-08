package com.knud4.an.exception;

public class CookieNotFoundException extends RuntimeException {

    public CookieNotFoundException(String message) {
        super(message);
    }
}
