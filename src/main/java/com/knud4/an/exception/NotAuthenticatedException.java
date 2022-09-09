package com.knud4.an.exception;

public class NotAuthenticatedException extends RuntimeException{
    public NotAuthenticatedException(String message) {
        super(message);
    }
}
