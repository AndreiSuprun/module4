package com.epam.esm.restapp.exception;

import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<CustomErrorResponse> handleValidationException(ValidationException ex) {
        CustomErrorResponse apiResponse = new CustomErrorResponse();
        apiResponse.setErrorCode(ex.getErrorCode().getCode().toString());
        apiResponse.setErrorMessage(messageSource.getMessage(ex.getErrorCode().getMessageCode(), ex.getParams(), Locale.getDefault()));
        return new ResponseEntity<>(apiResponse, ex.getErrorCode().getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        CustomErrorResponse apiResponse = new CustomErrorResponse();
        apiResponse.setErrorCode(ErrorCode.REQUEST_BODY_MISMATCH.getCode().toString());
        apiResponse.setErrorMessage(messageSource.getMessage(ErrorCode.REQUEST_BODY_MISMATCH.getMessageCode(),
                new Object[] {ex.getMessage()}, Locale.getDefault()));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<CustomErrorResponse> handleDataAccessException(DataAccessException ex) {
        CustomErrorResponse apiResponse = new CustomErrorResponse();
        apiResponse.setErrorCode(ErrorCode.INTERNAL_ERROR.getCode().toString());
        apiResponse.setErrorMessage(messageSource.getMessage(ErrorCode.INTERNAL_ERROR.getMessageCode(),
                new Object[] {}, Locale.getDefault()));
        return new ResponseEntity<>(apiResponse, ErrorCode.INTERNAL_ERROR.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        CustomErrorResponse apiResponse = new CustomErrorResponse();
        apiResponse.setErrorCode(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH.getCode().toString());
        apiResponse.setErrorMessage(messageSource.getMessage(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH.getMessageCode(),
                new Object[] {ex.getName()}, Locale.getDefault()));
        return new ResponseEntity<>(apiResponse, ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH.getHttpStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<CustomErrorResponse> handleException(Exception ex) {
        CustomErrorResponse apiResponse = new CustomErrorResponse();
        apiResponse.setErrorCode(ErrorCode.BAD_REQUEST.getCode().toString());
        apiResponse.setErrorMessage(messageSource.getMessage(ErrorCode.BAD_REQUEST.getMessageCode(),
                new Object[] {ex.toString()}, Locale.getDefault()));
        return new ResponseEntity<>(apiResponse, ErrorCode.BAD_REQUEST.getHttpStatus());
    }
}