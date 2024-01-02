package com.example.test.exception;

public class TokenAuthenticationException extends RuntimeException {
    public TokenAuthenticationException(String message) {
        super(message);
    }
}
