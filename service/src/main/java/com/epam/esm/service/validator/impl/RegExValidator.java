package com.epam.esm.service.validator.impl;

import com.epam.esm.service.validator.Validator;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Base class for validation with regex
 */
public class RegExValidator implements Validator<String> {

    private int maxLength;
    private Pattern pattern;

    public RegExValidator(String regex, int maxLength) {
        this.maxLength = maxLength;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isValid(String str) {
        return Objects.nonNull(str)
                && str.length() <= maxLength
                && pattern.matcher(str).matches();
    }
}
