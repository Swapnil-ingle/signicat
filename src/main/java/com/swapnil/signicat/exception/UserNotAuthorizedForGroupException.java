package com.swapnil.signicat.exception;

public class UserNotAuthorizedForGroupException extends SignicatRuntimeException {
    private static final String EXCEPTION_MSG = "User (%s) is not authorized to operate on group (%s)";

    public UserNotAuthorizedForGroupException(String username, Long groupId) {
        super(String.format(EXCEPTION_MSG, username, groupId));
    }
}
