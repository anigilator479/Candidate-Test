package com.example.test.exception;

public class TokenAuthenticationException extends RuntimeException {
    public TokenAuthenticationException(String expiredOrInvalidJwtToken) {
        super(expiredOrInvalidJwtToken);
    }
}
