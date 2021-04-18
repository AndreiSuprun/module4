package com.epam.esm.service.validator;

import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;

import java.util.LinkedList;
import java.util.List;

public abstract class EntityValidator {

    private static final String DELIMITER = " - ";

    public List<String> validate(Object object) {
        List<String> invalidFields = new LinkedList<>();
        validateObject(invalidFields, object);
        return invalidFields;
    }

    protected abstract void validateObject(List<String> invalidFields,
                                           Object object);

    protected <T> void validateField(Validator<T> validator,
                                     T field,
                                     List<String> invalidFields, String... params) {
        if (!validator.isValid(field)) {
            if (params.length == 2) {
                invalidFields.add(params[0] + DELIMITER + params[1]);
            }
        }
    }
}

