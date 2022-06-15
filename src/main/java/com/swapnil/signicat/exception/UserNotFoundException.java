package com.swapnil.signicat.exception;

public class UserNotFoundException extends SignicatRuntimeException {
    private static final String EXCEPTION_MSG = "User with username or id '%s' does not exist";

    public UserNotFoundException(Long userId) {
        super(String.format(EXCEPTION_MSG, userId));
    }

    public UserNotFoundException(String username) {
        super(String.format(EXCEPTION_MSG, username));
    }

    public UserNotFoundException(String username, Exception e) {
        super(String.format(EXCEPTION_MSG, username), e);
    }
}
