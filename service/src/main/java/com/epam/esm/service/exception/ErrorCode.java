package com.epam.esm.service.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    BAD_REQUEST(40008, "bad.request", HttpStatus.BAD_REQUEST),
    METHOD_ARGUMENT_TYPE_MISMATCH(40007, "method.argument.type.mismatch", HttpStatus.BAD_REQUEST),
    REQUEST_BODY_MISMATCH(40009, "request.body.mismatch", HttpStatus.BAD_REQUEST),
    USER_NAME_OR_PASSWORD_NOT_VALID(40102, "user.name.or.password.not.valid", HttpStatus.BAD_REQUEST),
    CERTIFICATE_NOT_FOUND(40401, "certificate.not.found", HttpStatus.NOT_FOUND),
    CERTIFICATES_NOT_FOUND(40405, "certificates.not.found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(40406, "user.not.found", HttpStatus.NOT_FOUND),
    USER_NOT_ADDED(40412, "user.not.added", HttpStatus.NOT_FOUND),
    INVALID_JWT_SIGNATURE(40016, "invalid.jwt.signature", HttpStatus.BAD_REQUEST),
    INVALID_JWT_TOKEN(40017, "invalid.jwt.token", HttpStatus.BAD_REQUEST),
    EXPIRED_JWT_TOKEN(40018, "expired.jwt.token", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_JWT_TOKEN(40019, "unsupported.jwt.token", HttpStatus.BAD_REQUEST),
    JWT_CLAIMS_ARE_EMPTY(40020, "jwt.claims.empty", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXIST(40406, "user.already.exist", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(40407, "order.not.found", HttpStatus.NOT_FOUND),
    TAG_NOT_FOUND(40402, "tag.not.found", HttpStatus.NOT_FOUND),
    TAG_IS_EMPTY(40009, "tag.is.empty", HttpStatus.BAD_REQUEST),
    CERTIFICATE_CANNOT_BE_DELETED(40006, "certificate.cannot.be.deleted", HttpStatus.BAD_REQUEST),
    TAG_CANNOT_BE_DELETED(40006, "tag.cannot.be.deleted", HttpStatus.BAD_REQUEST),
    TAG_FIELD_INVALID(40001, "tag.field.invalid", HttpStatus.BAD_REQUEST),
    TAG_ALREADY_IN_DB(40003, "tag.already.in.db", HttpStatus.BAD_REQUEST),
    CERTIFICATE_ALREADY_IN_DB(40004, "certificate.already.in.db", HttpStatus.BAD_REQUEST),
    CERTIFICATE_FIELD_INVALID(40002, "certificate.field.invalid", HttpStatus.BAD_REQUEST),
    USER_FIELD_INVALID(40012, "user.field.invalid", HttpStatus.BAD_REQUEST),
    ORDER_FIELD_INVALID(40011, "order.field.invalid", HttpStatus.BAD_REQUEST),
    CERTIFICATES_NOT_ADDED(40012, "certificates.not.added", HttpStatus.BAD_REQUEST),
    ORDER_ITEMS_NOT_ADDED(40013, "order.items.not.added", HttpStatus.BAD_REQUEST),
    QUERY_PARAMETER_INVALID(40005, "query.parameter.invalid", HttpStatus.BAD_REQUEST),
    PAGE_SIZE_INVALID(400013, "page.size.invalid", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(50001, "internal.error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED_USER(40101, "unauthorized.user", HttpStatus.UNAUTHORIZED);

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
