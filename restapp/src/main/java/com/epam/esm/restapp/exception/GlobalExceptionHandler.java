package com.epam.esm.restapp.exception;

import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({ProjectException.class})
    public ResponseEntity<CustomErrorResponse> handleProjectException(ProjectException ex, WebRequest request) {
        CustomErrorResponse apiResponse = new CustomErrorResponse();
        apiResponse.setErrorCode(ex.getErrorCode().getCode().toString());
        apiResponse.setErrorMessage(messageSource.getMessage(ex.getErrorCode().getMessageCode(), ex.getParams(), Locale.getDefault()));
        return new ResponseEntity<>(apiResponse, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<CustomErrorResponse> handleProjectException(DataAccessException ex, WebRequest request) {
        CustomErrorResponse apiResponse = new CustomErrorResponse();
        apiResponse.setErrorCode(ErrorCode.INTERNAL_ERROR.getCode().toString());
        apiResponse.setErrorMessage(messageSource.getMessage(ErrorCode.INTERNAL_ERROR.getMessageCode(), new Object[] {ex.toString()}, Locale.getDefault()));
        return new ResponseEntity<>(apiResponse, ErrorCode.INTERNAL_ERROR.getHttpStatus());
    }
}