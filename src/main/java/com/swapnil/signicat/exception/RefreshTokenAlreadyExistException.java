package com.swapnil.signicat.exception;

public class RefreshTokenAlreadyExistException extends SignicatRuntimeException {
    private static final String EXCEPTION_MSG = "Refresh Token '%s' already exists";

    public RefreshTokenAlreadyExistException(String refreshToken) {
        super(String.format(EXCEPTION_MSG, refreshToken));
    }

    public RefreshTokenAlreadyExistException(String refreshToken, Exception e) {
        super(String.format(EXCEPTION_MSG, refreshToken), e);
    }
}
