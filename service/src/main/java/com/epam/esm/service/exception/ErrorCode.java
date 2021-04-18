package com.epam.esm.service.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    CERTIFICATE_NOT_FOUND(40401, "certificate.not.found", HttpStatus.NOT_FOUND),
    TAG_NOT_FOUND(40402, "tag.not.found", HttpStatus.NOT_FOUND),
    TAG_FIELD_INVALID(40001, "tag.field.invalid", HttpStatus.BAD_REQUEST),
    TAG_ALREADY_IN_DB(40003, "tag.already.in.db", HttpStatus.BAD_REQUEST),
    CERTIFICATE_FIELD_INVALID(40002, "certificate.field.invalid", HttpStatus.BAD_REQUEST),
    TAG_UPDATE_NOT_ALLOWED(40501, "tag.update.notallowed", HttpStatus.METHOD_NOT_ALLOWED),
    INTERNAL_ERROR(50001, "internal.error", HttpStatus.INTERNAL_SERVER_ERROR);

    private Integer code;
    private String messageCode;
    private HttpStatus httpStatus;

    ErrorCode(Integer code, String messageCode, HttpStatus httpStatus){
        this.code = code;
        this.messageCode = messageCode;
        this.httpStatus = httpStatus;

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
