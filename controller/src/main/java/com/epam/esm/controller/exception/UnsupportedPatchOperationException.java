package com.epam.esm.controller.exception;

import java.util.Locale;
import java.util.Set;

public class UnsupportedPatchOperationException extends RuntimeException{

    private final String messageKey;
    private final Locale locale;
    private Set<String> fields;

    public UnsupportedPatchOperationException(String messageKey, Set<String> fields) {
        this(messageKey, Locale.getDefault());
        this.fields = fields;
    }

    public UnsupportedPatchOperationException(String messageKey, Locale locale) {
        this.messageKey = messageKey;
        this.locale = locale;
    }

    public String getLocalizedMessage() {
        return Messages.getMessageForLocale(messageKey, locale).concat(fields.toString());
    }
}
