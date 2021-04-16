package com.epam.esm.restapp.exception;

import java.util.Locale;

public class NotFoundException extends RuntimeException{

    private final String messageKey;
    private final Locale locale;
    private long id;

    public NotFoundException(String messageKey, long id) {
        this(messageKey, Locale.getDefault());
        this.id = id;
    }

    public NotFoundException(String messageKey, Locale locale) {
        this.messageKey = messageKey;
        this.locale = locale;
    }

    public String getLocalizedMessage() {
        return Messages.getMessageForLocale(messageKey, locale).concat(String.valueOf(id));
    }
}
