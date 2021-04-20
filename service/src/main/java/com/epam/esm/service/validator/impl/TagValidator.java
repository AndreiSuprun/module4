package com.epam.esm.service.validator.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.stereotype.Service;

@Service
public class TagValidator extends EntityValidator<Tag> {

    private final static String NAME_FIELD = "name";

    @Override
    public void validate(Tag tag) {
        validateField(new NameValidator(), tag.getName(), NAME_FIELD, tag.getName());
    }
}
