package com.epam.esm.service.exception;

public enum ErrorCode {
    CERTIFICATE_NOT_FOUND(40401, "certificate.not.found"),
    TAG_NOT_FOUND(40402, "tag.not.found"),
    TAG_FIELD_INVALID(40403, "tag.field.invalid"),
    CERTIFICATE_FIELD_INVALID(40404, "certificate.field.invalid");


    private Integer code;
    private String messageCode;

    ErrorCode(Integer code, String messageCode){
        this.code = code;
        this.messageCode = messageCode;
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
}
