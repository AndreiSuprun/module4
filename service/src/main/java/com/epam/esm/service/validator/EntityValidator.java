package com.epam.esm.service.validator;

import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;

/**
 * Abstract class for entity validation
 *
 * @author Andrei Suprun
 */
public abstract class EntityValidator<T> {

    /**
     * Check if input object is valid
     *
     * @param object object to validate
     * @throws ValidationException if validation is failed
     */
    public abstract void validate(T object);

    /**
     * Check if input field is valid
     *
     * @param validator that is needed to check field
     * @param field field to validate
     * @param errorCode code of error to set if validation is failed
     * @param params parameters to pass to error message
     * @throws ValidationException if validation is failed
     */
    protected <V> void validateField(Validator<V> validator,
                                     V field, ErrorCode errorCode, Object... params) {
        if (!validator.isValid(field)) {
            throw new ValidationException(errorCode, params);
            }
        }
    }


