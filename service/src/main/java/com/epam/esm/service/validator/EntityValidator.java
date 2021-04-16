package com.epam.esm.service.validator;

import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;

import java.util.LinkedList;
import java.util.List;

public abstract class EntityValidator {

    public List<ProjectException> validate(Object object) {
        List<ProjectException> errors = new LinkedList<>();
        validateObject(errors, object);
        return errors;
    }

    protected abstract void validateObject(List<ProjectException> errors,
                                           Object object);

    protected <T> void validateField(Validator<T> validator,
                                     T field,
                                     List<ProjectException> errors, String... params) {
        if (!validator.isValid(field)){
            errors.add(new ProjectException(ErrorCode.CERTIFICATE_NOT_FOUND, params));}
        }
    }

