package com.swapnil.signicat.exception;

public class UserGroupAlreadyExistException extends SignicatRuntimeException {
    private static final String EXCEPTION_MSG = "User Group (%s) already exist";

    public UserGroupAlreadyExistException(String groupName) {
        super(String.format(EXCEPTION_MSG, groupName));
    }
}
