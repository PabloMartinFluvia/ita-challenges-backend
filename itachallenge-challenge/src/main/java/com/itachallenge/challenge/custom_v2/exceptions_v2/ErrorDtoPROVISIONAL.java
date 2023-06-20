package com.itachallenge.challenge.custom_v2.exceptions_v2;

import java.util.Set;

/*
OBS: provisional class. TODO: decide type returned when exception handled
 */
public class ErrorDtoPROVISIONAL {

    private Set<String> errorsMsg;

    private int httpStatus;

    public ErrorDtoPROVISIONAL(Set<String> errorsMsg, int httpStatus) {
        this.errorsMsg = errorsMsg;
        this.httpStatus = httpStatus;
    }

    public Set<String> getErrorsMsg() {
        return errorsMsg;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
