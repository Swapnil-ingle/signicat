package com.swapnil.signicat.exception;

public class RefreshTokenMissingException extends SignicatRuntimeException {
    private static final String EXCEPTION_MSG = "Refresh token '%s' does not exist";

    public RefreshTokenMissingException(String refreshToken) {
        super(String.format(EXCEPTION_MSG, refreshToken));
    }

    public RefreshTokenMissingException(String refreshToken, Exception e) {
        super(String.format(EXCEPTION_MSG, refreshToken), e);
    }
}
