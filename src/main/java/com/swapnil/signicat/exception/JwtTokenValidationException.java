package com.swapnil.signicat.exception;

public class JwtTokenValidationException extends SignicatRuntimeException {
    private static final String EXCEPTION_MSG = "Error while validating JWT token from Authorization header: %s";

    public JwtTokenValidationException(String token) {
        super(String.format(EXCEPTION_MSG, token));
    }

    public JwtTokenValidationException(String token, Exception e) {
        super(String.format(EXCEPTION_MSG, token), e);
    }
}
