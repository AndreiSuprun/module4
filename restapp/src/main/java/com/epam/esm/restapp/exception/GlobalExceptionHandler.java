package com.epam.esm.restapp.exception;

import com.epam.esm.service.exception.ProjectException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ProjectException.class})
    public ResponseEntity<CustomErrorResponse> entityNotFound(ProjectException ex, WebRequest request) {
        CustomErrorResponse apiResponse = new CustomErrorResponse();
        apiResponse.setErrorCode("40401");
        apiResponse.setErrorMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(apiResponse, ex.getErrorCode().);
    }

    @ExceptionHandler({UnsupportedPatchOperationException.class})
    public ResponseEntity<CustomErrorResponse> entityNotFound(UnsupportedPatchOperationException ex, WebRequest request) {
        CustomErrorResponse apiResponse = new CustomErrorResponse();
        apiResponse.setErrorCode("40401");
        apiResponse.setErrorMessage(ex.getLocalizedMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errorCode", "40001");
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errorMessage", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        CustomErrorResponse apiResponse = new CustomErrorResponse();
        apiResponse.setErrorCode("40001");
        apiResponse.setErrorMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(
                apiResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}