package com.swapnil.signicat.exception;

public class SignicatRuntimeException extends RuntimeException {
    public SignicatRuntimeException(String msg) {
        super(msg);
    }

    public SignicatRuntimeException(String msg, Exception e) {
        super(msg, e);
    }
}
