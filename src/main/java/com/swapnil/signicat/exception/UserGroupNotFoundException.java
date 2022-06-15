package com.swapnil.signicat.exception;

public class UserGroupNotFoundException extends SignicatRuntimeException {
    private static final String EXCEPTION_MSG = "UserGroup with the name or id '%s' does not exist";

    public UserGroupNotFoundException(Long groupId) {
        super(String.format(EXCEPTION_MSG, groupId));
    }
}
