package com.epam.esm.service.exception;

public class ValidationException extends RuntimeException{

    private final ErrorCode errorCode;
    private final Object[] params;

    public ValidationException(ErrorCode errorCode, Object... params) {
        this.errorCode = errorCode;
        this.params = params;
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }
    public Object[] getParams(){
        return params;
    }
}
