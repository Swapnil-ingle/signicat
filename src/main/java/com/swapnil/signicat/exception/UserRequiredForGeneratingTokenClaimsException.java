package com.swapnil.signicat.exception;

public class UserRequiredForGeneratingTokenClaimsException extends SignicatRuntimeException {
    private static final String EXCEPTION_MSG = "User (%s) should exist before generating token";

    public UserRequiredForGeneratingTokenClaimsException(String msg) {
        super(String.format(EXCEPTION_MSG, msg));
    }

    public UserRequiredForGeneratingTokenClaimsException(String msg, Exception e) {
        super(String.format(EXCEPTION_MSG, msg), e);
    }
}
